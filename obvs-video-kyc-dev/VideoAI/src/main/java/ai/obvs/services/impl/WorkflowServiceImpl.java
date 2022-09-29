package ai.obvs.services.impl;

import ai.obvs.Enums.DocumentName;
import ai.obvs.Enums.WorkflowStepType;
import ai.obvs.dto.workflow.DocumentDto;
import ai.obvs.dto.workflow.WorkflowDto;
import ai.obvs.dto.workflow.WorkflowStepDto;
import ai.obvs.mapper.DocumentMapper;
import ai.obvs.mapper.WorkflowMapper;
import ai.obvs.mapper.WorkflowStepMapper;
import ai.obvs.model.*;
import ai.obvs.repository.DocumentRepository;
import ai.obvs.repository.WorkflowRepository;
import ai.obvs.repository.WorkflowStepDocumentsRepository;
import ai.obvs.repository.WorkflowStepRepository;
import ai.obvs.services.DocumentService;
import ai.obvs.services.OrgService;
import ai.obvs.services.WorkflowService;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
public class WorkflowServiceImpl implements WorkflowService {

    private OrgService orgService;
    private WorkflowRepository workflowRepository;
    private WorkflowStepRepository workflowStepRepository;
    private WorkflowStepDocumentsRepository workflowStepDocumentsRepository;
    private DocumentService documentService;

    public WorkflowServiceImpl(OrgService orgService, WorkflowRepository workflowRepository, WorkflowStepRepository workflowStepRepository,
                               WorkflowStepDocumentsRepository workflowStepDocumentsRepository, DocumentService documentService) {
        this.orgService = orgService;
        this.workflowRepository = workflowRepository;
        this.workflowStepRepository = workflowStepRepository;
        this.workflowStepDocumentsRepository = workflowStepDocumentsRepository;
        this.documentService = documentService;
    }

    @Override
    public WorkflowDto save(Long orgId, WorkflowDto workflowDto) {
        Workflow workflow = WorkflowMapper.MAPPER.ToWorkflow(workflowDto);
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);

        if (organizationById.isPresent()) {
            Organization organization = organizationById.get();
            workflow.setOrganization(organization);
        }
        List<WorkflowStepDto> steps = workflowDto.getSteps();

        Workflow savedWorkflow = workflowRepository.save(workflow);
        WorkflowDto resultWorkflow = WorkflowMapper.MAPPER.ToWorkflowDto(workflow);
        List<WorkflowStep> workflowSteps = WorkflowStepMapper.MAPPER.ToWorkflowStepList(steps);
        workflowSteps.forEach(workflowStep -> workflowStep.setWorkflow(workflow));
        List<WorkflowStep> savedWorkflowSteps = workflowStepRepository.saveAll(workflowSteps);
        List<WorkflowStepDto> workflowStepDtos = new ArrayList<>();
        if (steps != null) {
            steps.forEach(step -> {
                if (step.getWorkflowStepType().equals(WorkflowStepType.DOCUMENT)) {
                    List<DocumentDto> allowedDocuments = step.getAllowedDocuments();
                    Optional<WorkflowStep> optionalWorkflowStep = savedWorkflowSteps.stream()
                            .filter(x -> x.getOrderNumber() == step.getOrderNumber()).findAny();
                    if (optionalWorkflowStep.isPresent()) {
                        WorkflowStep workflowStep = optionalWorkflowStep.get();
                        List<DocumentName> documentNames = allowedDocuments.stream().map(documentDto -> documentDto.getName()).collect(Collectors.toList());
                        saveDocuments(orgId, documentNames, workflowStep);
                        WorkflowStepDto workflowStepDto = WorkflowStepMapper.MAPPER.ToWorkflowStepDto(workflowStep);
                        workflowStepDto.setAllowedDocuments(allowedDocuments);
                        workflowStepDtos.add(workflowStepDto);
                    }
                }
            });

            resultWorkflow.setSteps(workflowStepDtos);
        }

        return resultWorkflow;
    }

    @Override
    public WorkflowDto getWorkflow(Long orgId, String workflowName) {
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);

        if (organizationById.isPresent()) {
            Organization organization = organizationById.get();
            Optional<Workflow> byOrganizationAndName = workflowRepository.findByOrganizationAndName(organization, workflowName);
            if (byOrganizationAndName.isPresent()) {
                Workflow workflow = byOrganizationAndName.get();
                WorkflowDto workflowDto = WorkflowMapper.MAPPER.ToWorkflowDto(workflow);

                List<WorkflowStep> allByWorkflow = workflowStepRepository.findAllByWorkflow(workflow);
                List<WorkflowStepDto> workflowStepDtos = WorkflowStepMapper.MAPPER.ToWorkflowStepDtoList(allByWorkflow);
                workflowDto.setSteps(workflowStepDtos);
                return workflowDto;
            }
        }
        return null;
    }

    @Override
    public void add(Long orgId, String workflowName, WorkflowStepDto workflowStepDto) {

        WorkflowStep workflowStep = WorkflowStepMapper.MAPPER.ToWorkflowStep(workflowStepDto);
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);

        if (organizationById.isPresent()) {
            Organization organization = organizationById.get();
            Optional<Workflow> byOrganizationAndName = workflowRepository.findByOrganizationAndName(organization, workflowName);
            if (byOrganizationAndName.isPresent()) {
                workflowStep.setWorkflow(byOrganizationAndName.get());
                workflowStepRepository.save(workflowStep);
            }
        }
    }

    @Override
    public void addAll(Long orgId, String workflowName, List<WorkflowStepDto> workflowStepDtoList) {
        List<WorkflowStep> workflowSteps = WorkflowStepMapper.MAPPER.ToWorkflowStepList(workflowStepDtoList);
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);

        if (organizationById.isPresent()) {
            Organization organization = organizationById.get();
            Optional<Workflow> byOrganizationAndName = workflowRepository.findByOrganizationAndName(organization, workflowName);

            if (byOrganizationAndName.isPresent()) {
                Workflow workflow = byOrganizationAndName.get();
                workflowSteps.forEach(step -> step.setWorkflow(workflow));
                workflowStepRepository.saveAll(workflowSteps);
            }
        }
    }

    @Override
    public void addAll(Long orgId, String workflowName, Long stepId, List<DocumentName> documentNames) {
        Optional<WorkflowStep> workflowStepOptional = getWorkflowStep(orgId, workflowName, stepId);
        if (workflowStepOptional.isPresent()) {
            WorkflowStep workflowStep = workflowStepOptional.get();
            saveDocuments(orgId, documentNames, workflowStep);
        }
    }

    private List<WorkflowStepDocument> saveDocuments(Long orgId, List<DocumentName> documentNames, WorkflowStep workflowStep) {
        List<Document> documentsByName = documentService.getDocumentsByName(orgId, documentNames);
        List<WorkflowStepDocument> workflowStepDocumentList = new ArrayList<>();
        documentsByName.forEach(document -> {
            WorkflowStepDocument workflowStepDocument = new WorkflowStepDocument();
            workflowStepDocument.setWorkflowStepId(workflowStep.getId());
            workflowStepDocument.setDocumentId(document.getId());
            workflowStepDocumentList.add(workflowStepDocument);
        });
        List<WorkflowStepDocument> savedDocumentList = workflowStepDocumentsRepository.saveAll(workflowStepDocumentList);
        return savedDocumentList;
    }

    @Override
    public List<Document> getAll(Long orgId, String workflowName, Long stepId) {
        Optional<WorkflowStep> workflowStepOptional = getWorkflowStep(orgId, workflowName, stepId);
        if (workflowStepOptional.isPresent()) {
            WorkflowStep workflowStep = workflowStepOptional.get();
            List<WorkflowStepDocument> workflowStepDocuments = workflowStepDocumentsRepository.findAllByWorkflowStepId(workflowStep.getId());
            List<Long> documentIds = workflowStepDocuments.stream().map(workflowStepDocument -> workflowStepDocument.getDocumentId()).collect(Collectors.toList());

            List<Document> documentsById = documentService.getDocumentsById(orgId, documentIds);
            return documentsById;
        }
        return new ArrayList<>();
    }

    @Override
    public List<WorkflowStepDto> getWorkflowSteps(Long orgId, String workflowName) {
        List<WorkflowStepDto> workflowStepDtoList = new ArrayList<>();
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
        if (organizationById.isPresent()) {
            Organization organization = organizationById.get();
            Optional<Workflow> workflowOptional = workflowRepository.findByOrganizationAndName(organization, workflowName);
            if (workflowOptional.isPresent()) {
                Workflow workflow = workflowOptional.get();
                List<WorkflowStep> workflowSteps = workflowStepRepository.findAllByWorkflow(workflow);
                workflowSteps.forEach(workflowStep -> {
                    WorkflowStepDto workflowStepDto = WorkflowStepMapper.MAPPER.ToWorkflowStepDto(workflowStep);
                    List<Document> documents = getAll(orgId, workflowName, workflowStep.getId());
                    workflowStepDto.setAllowedDocuments(DocumentMapper.MAPPER.ToDocumentDtoList(documents));
                    workflowStepDtoList.add(workflowStepDto);
                });
            }
        }
        return workflowStepDtoList;
    }

    @Override
    public Optional<WorkflowStep> getWorkflowStep(Long orgId, String workflowName, Long stepId) {
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
        if (organizationById.isPresent()) {
            Organization organization = organizationById.get();
            Optional<Workflow> workflowOptional = workflowRepository.findByOrganizationAndName(organization, workflowName);
            if (workflowOptional.isPresent()) {
                Workflow workflow = workflowOptional.get();
                List<WorkflowStep> workflowSteps = workflowStepRepository.findAllByWorkflow(workflow);
                Optional<WorkflowStep> workflowStepOptional = workflowSteps.stream().filter(workflowStep -> workflowStep.getId().equals(stepId)).findAny();
                return workflowStepOptional;
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<WorkflowStep> getWorkflowStep(Long orgId, String workflowName, String workflowStepName) {
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
        if (organizationById.isPresent()) {
            Organization organization = organizationById.get();
            Optional<Workflow> workflowOptional = workflowRepository.findByOrganizationAndName(organization, workflowName);
            if (workflowOptional.isPresent()) {
                Workflow workflow = workflowOptional.get();
                List<WorkflowStep> workflowSteps = workflowStepRepository.findAllByWorkflow(workflow);
                Optional<WorkflowStep> workflowStepOptional = workflowSteps.stream()
                        .filter(workflowStep -> workflowStep.getName().equals(workflowStepName)).findAny();
                return workflowStepOptional;
            }
        }
        return Optional.empty();
    }
}
