package ai.obvs.controllers;

import ai.obvs.Enums.DocumentName;
import ai.obvs.dto.workflow.DocumentDto;
import ai.obvs.dto.workflow.DocumentRequestDto;
import ai.obvs.dto.workflow.WorkflowDto;
import ai.obvs.dto.workflow.WorkflowStepDto;
import ai.obvs.mapper.DocumentMapper;
import ai.obvs.model.Document;
import ai.obvs.model.Organization;
import ai.obvs.services.DocumentService;
import ai.obvs.services.OrgService;
import ai.obvs.services.WorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("/v1/workflow")
public class WorkflowController {

    private WorkflowService workflowService;
    private DocumentService documentService;

    public WorkflowController(WorkflowService workflowService, DocumentService documentService) {
        this.workflowService = workflowService;
        this.documentService = documentService;
    }

    @PostMapping("/")
    public ResponseEntity<?> saveWorkflow(@RequestHeader("x-obvs-org") Long orgId, @RequestBody WorkflowDto workflowDto) {
        WorkflowDto workflow = workflowService.save(orgId, workflowDto);
        return ResponseEntity.ok(workflow);
    }

    @GetMapping("{name}")
    public ResponseEntity<?> getWorkflow(@RequestHeader("x-obvs-org") Long orgId, @PathVariable(value = "name") String name) {
        WorkflowDto workflow = workflowService.getWorkflow(orgId, name);
        return ResponseEntity.ok(workflow);
    }

    @GetMapping("{name}/steps")
    public ResponseEntity<?> getSteps(@RequestHeader("x-obvs-org") Long orgId, @PathVariable(value = "name") String name) {
        List<WorkflowStepDto> workflowSteps = workflowService.getWorkflowSteps(orgId, name);
        return ResponseEntity.ok(workflowSteps);
    }

    @PostMapping("{name}/steps/add")
    public ResponseEntity<?> addStep(@RequestHeader("x-obvs-org") Long orgId, @PathVariable(value = "name") String name,
                                     @Valid @RequestBody WorkflowStepDto workflowStepDto) {
        workflowService.add(orgId, name, workflowStepDto);
        return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.CREATED);
    }

    @PostMapping("{name}/steps/addAll")
    public ResponseEntity<?> addAllSteps(@RequestHeader("x-obvs-org") Long orgId, @PathVariable(value = "name") String name,
                                         @Valid @RequestBody List<WorkflowStepDto> workflowStepDtoList) {
        workflowService.addAll(orgId, name, workflowStepDtoList);
        return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.CREATED);
    }

    @PostMapping("{name}/steps/{stepId}/documents")
    public ResponseEntity<?> addStepDocuments(@RequestHeader("x-obvs-org") Long orgId,
                                              @PathVariable(value = "name") String name,
                                              @PathVariable(value = "stepId") Long stepId,
                                              @Valid @RequestBody List<DocumentName> documentNames) {
        workflowService.addAll(orgId, name, stepId, documentNames);
        return ResponseEntity.ok("");
    }

    @GetMapping("{name}/steps/{stepId}/documents")
    public ResponseEntity<?> getStepDocuments(@RequestHeader("x-obvs-org") Long orgId,
                                          @PathVariable(value = "name") String name,
                                          @PathVariable(value = "stepId") Long stepId) {
        List<Document> documents = workflowService.getAll(orgId, name, stepId);
        List<DocumentDto> documentDtos = DocumentMapper.MAPPER.ToDocumentDtoList(documents);
        return ResponseEntity.ok(documentDtos);
    }

    @GetMapping("/documents/list")
    public ResponseEntity<?> getDocuments(@RequestHeader("x-obvs-org") Long orgId) {
            List<Document> documentsByCountry = documentService.getDocumentsByCountry(orgId);
            return ResponseEntity.ok(documentsByCountry);
    }

    @PostMapping("/documents/addAll")
    public ResponseEntity<?> addAllDocuments(@RequestBody DocumentRequestDto documentRequestDto) {
        documentService.saveDocuments(documentRequestDto.getCountry(), documentRequestDto.getDocuments());
        return ResponseEntity.ok("");
    }
}
