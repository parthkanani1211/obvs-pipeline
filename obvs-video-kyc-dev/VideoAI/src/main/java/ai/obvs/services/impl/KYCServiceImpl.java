package ai.obvs.services.impl;

import ai.obvs.Enums.DocumentName;
import ai.obvs.Enums.KYCResourceType;
import ai.obvs.Enums.PanField;
import ai.obvs.Enums.WorkflowStepType;
import ai.obvs.dto.Aadhar.AadharDataValidationDto;
import ai.obvs.dto.*;
import ai.obvs.dto.Aadhar.GenerateOTPDto;
import ai.obvs.dto.workflow.DocumentDto;
import ai.obvs.dto.workflow.WorkflowStepDto;
import ai.obvs.mapper.DocumentMapper;
import ai.obvs.mapper.KYCMapper;
import ai.obvs.mapper.KYCResourceMapper;
import ai.obvs.mapper.WorkflowStepMapper;
import ai.obvs.model.*;
import ai.obvs.repository.KYCRepository;
import ai.obvs.repository.ResourceRepository;
import ai.obvs.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class KYCServiceImpl implements KYCService {

    private KYCRepository kycRepository;
    private KYCDataService kycDataService;
    private ResourceRepository resourceRepository;
    private CustomerService customerService;

    private PANCardService panCardService;
    private AadharCardService aadharCardService;
    private FaceRecognitionService faceRecognitionService;
    private SignatureService signatureService;
    private WorkflowService workflowService;
    private DocumentService documentService;

    public KYCServiceImpl(KYCRepository kycRepository, ResourceRepository resourceRepository, CustomerService customerService,
                          PANCardService panCardService, AadharCardService aadharCardService, FaceRecognitionService faceRecognitionService,
                          SignatureService signatureService, WorkflowService workflowService,
                          DocumentService documentService) {
        this.kycRepository = kycRepository;
        this.resourceRepository = resourceRepository;
        this.customerService = customerService;
        this.panCardService = panCardService;
        this.aadharCardService = aadharCardService;
        this.faceRecognitionService = faceRecognitionService;
        this.signatureService = signatureService;
        this.workflowService = workflowService;
        this.documentService = documentService;
    }

    private void updateCustomerDetails(Long customerId, String customerName) {
        Optional<Customer> customerOptional = customerService.getById(customerId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            if (!StringUtils.isEmpty(customerName)) {
                String[] names = customerName.trim().split(" ");
                if (names.length > 1) {
                    customer.setFirstName(names[0]);
                    customer.setLastName(names[1]);
                } else if (names.length > 0) {
                    customer.setFirstName(names[0]);
                    customer.setLastName("");
                }
                customerService.update(customer);
            }
        }
    }

    @Override
    public byte[] getContent(Long kycId) {
        Optional<KYC> optionalKYC = kycRepository.findById(kycId);
        if (optionalKYC.isPresent()) {
            Set<KYCResource> kycResources = optionalKYC.get().getResources();
            Optional<KYCResource> kycResource = kycResources.stream().filter(r -> r.getKycResourceType().equals(KYCResourceType.PAN)).findAny();
            if (kycResource.isPresent())
                return kycResource.get().getContent();
        }
        return null;
    }

    @Override
    public byte[] getContent(Long kycId, KYCResourceType kycResourceType) {
        Optional<KYC> optionalKYC = kycRepository.findById(kycId);
        if (optionalKYC.isPresent()) {
            Set<KYCResource> kycResources = optionalKYC.get().getResources();
            Optional<KYCResource> kycResource = kycResources.stream().filter(r -> r.getKycResourceType().equals(kycResourceType)).findAny();
            if (kycResource.isPresent())
                return kycResource.get().getContent();
        }
        return null;
    }

    @Override
    public KYCDto getKYC(Long videoSessionId, String workflowStep) {
        List<KYC> kycList = kycRepository.findAllByVideoSessionId(videoSessionId);
        if (!kycList.isEmpty()) {
//            Optional<KYC> panCardKYC = kycList.stream().filter(k -> k.getKycType().equals(kycType)).findFirst();
            Optional<KYC> kycOptional = kycList.stream()
                    .filter(k -> k.getWorkflowStep().getName().equals(workflowStep))
                    .findFirst();
            if (kycOptional.isPresent()) {
                KYC kyc = kycOptional.get();
                KYCDto kycDto = KYCMapper.MAPPER.ToKYCDto(kyc);
                kycDto.setVideoSessionId(kyc.getVideoSession().getId());
                return kycDto;
            }
        }
        return null;
    }

    @Override
    public List<KYCDto> getKYCDtoByVideoSessionId(Long videoSessionId) {
        List<KYC> kycList = getKYCByVideoSessionId(videoSessionId);
        List<KYCDto> kycDtos = KYCMapper.MAPPER.ToKYCDto(kycList);
        return kycDtos;
    }

    @Override
    public List<KYC> getKYCByVideoSessionId(Long videoSessionId) {
        List<KYC> kycList = kycRepository.findAllByVideoSessionId(videoSessionId);
        return kycList;
    }

    @Override
    public List<KYCResourcesDto> getKYCResourcesByVideoSessionId(Long videoSessionId) {
        List<KYC> kycList = kycRepository.findAllByVideoSessionId(videoSessionId);
        List<KYCResourcesDto> kycResourcesDtos = new ArrayList<>();
        kycList.stream().forEach(kyc -> {
            WorkflowStep workflowStep = kyc.getWorkflowStep();
            WorkflowStepDto workflowStepDto = WorkflowStepMapper.MAPPER.ToWorkflowStepDto(workflowStep);

            Document document = kyc.getDocument();
            DocumentDto documentDto = DocumentMapper.MAPPER.ToDocumentDto(document);

            KYCResourcesDto kycResourcesDto = KYCResourceMapper.MAPPER.ToKYCResourcesDto(kyc);
            kycResourcesDto.setWorkflowStepDto(workflowStepDto);
            kycResourcesDto.setDocumentDto(documentDto);

            kycResourcesDtos.add(kycResourcesDto);
        });

        return kycResourcesDtos;
    }

    @Override
    public KYCDto extractAndSave(Long orgId, Long videoSessionId, Long customerId, String workflowName, String workflowStep,
                                 DocumentName DocumentName, KYCRequestImageDto kycRequestImageDto, List<KYC> kycList) {
//        KYCType kycType = KYCType.valueOf(kycTypeValue.toUpperCase());
//        KYC kyc = prepareKYC(videoSessionId, kycType);
        KYC kyc = prepareKYC(orgId, videoSessionId, workflowName, workflowStep, DocumentName);
        saveResources(kycRequestImageDto, kyc);

        KYCVerificationData kycVerificationData = extractData(customerId, kyc, kycRequestImageDto, kycList);
        try {
            kyc.setKYCVerificationData(new ObjectMapper().writeValueAsString(kycVerificationData));
        } catch (JsonProcessingException e) {
        }
        KYC savedKYC = kycRepository.save(kyc);
        return prepareResponse(savedKYC, kycVerificationData);
    }

    private KYCVerificationData extractData(Long customerId, KYC kyc, KYCRequestImageDto kycRequestImageDto, List<KYC> kycList) {
        if (kyc.getWorkflowStep().getWorkflowStepType().equals(WorkflowStepType.OTHER)) {
            switch (ai.obvs.Enums.WorkflowStep.valueOf(kyc.getWorkflowStep().getName())) {
                case SIGNATURE:
                    KYCVerificationData kycVerificationData = signatureService.getData(kycRequestImageDto);
                    kyc.setVerified(kycVerificationData.isVerified());
                    return kycVerificationData;
//                    return getSignVerificationData(kyc.getData(), inputImageDto);
                case FACE:
                    Map<DocumentName, String> imageDataToCompare = new HashMap<>();
//                    Optional<KYC> optionalKYC = kycList.stream().filter(k -> k.getDocument().getName().equals(DocumentName.PAN)).findAny();
//                    if (optionalKYC.isPresent()) {
//                        KYC panKYC = optionalKYC.get();
//                        String photoImage = panCardService.getPhotoImage(panKYC);
//                        imageDataToCompare.put(DocumentName.PAN, photoImage);
//                    }
                    Optional<KYC> optionalAadharKYC = kycList.stream().filter(k -> k.getDocument().getName().equals(DocumentName.AADHAR)).findAny();
                    if (optionalAadharKYC.isPresent()) {
                        KYC aadharKYC = optionalAadharKYC.get();
                        String photoImage = aadharCardService.getVerificationPhotoImage(aadharKYC);
                        imageDataToCompare.put(DocumentName.AADHAR, photoImage);
                    }
                    kycVerificationData = faceRecognitionService.getData(kycRequestImageDto, imageDataToCompare);
                    kyc.setVerified(kycVerificationData.isVerified());
                    return kycVerificationData;

//                    return getFaceVerificationData(kyc.getData(), inputImageDto);
            }
        } else
            return getKYCVerificationDataBasedOnDocument(customerId, kyc, kycRequestImageDto);

//        switch (kycType) {
//            case PAN:
//                return panCardService.getData(kycRequestImageDto);
//            case AADHAR:
//                return aadharCardService.getData(kycRequestImageDto);
//            case FACE:
//                return faceRecognitionService.getData(kycRequestImageDto);
//            case SIGN:
//                return signatureService.getData(kycRequestImageDto);
//        }
        return new KYCVerificationData();
    }

    private KYCVerificationData getKYCVerificationDataBasedOnDocument(Long customerId, KYC kyc, KYCRequestImageDto kycRequestImageDto) {
        switch (kyc.getDocument().getName()) {
            case PAN:
                KYCVerificationData kycVerificationData = panCardService.getData(kyc, kycRequestImageDto);
//                PANCardInfo panCardInfo = getPanCardInfo(kyc.getData(), inputImageDto);
                Optional<KYCDataMatchResult> kycDataMatchResult = kycVerificationData.getMatchResults().stream()
                        .filter(data -> data.getName().equals(PanField.NAME.getValue())).findAny();
                if (kycDataMatchResult.isPresent()) {
                    updateCustomerDetails(customerId, kycDataMatchResult.get().getValue());
                }
                return kycVerificationData;
//                return getPanCardVerificationData(panCardInfo);
            case AADHAR:
                return aadharCardService.getData(kyc, kycRequestImageDto);
//            return getAadharCardVerificationData(kyc.getData(), inputImageDto);
            case PASSPORT:
                break;
            case LICENSE:
                break;
            case VOTER_ID:
                break;
        }
        return null;
    }

    @Override
    public KYCVerificationData verifyPan(Long videoSessionId, KYCVerificationData kycVerificationData) {
        List<KYC> kycList = kycRepository.findAllByVideoSessionId(videoSessionId);
        Optional<KYC> optionalKYC = kycList.stream().filter(kyc -> kyc.getDocument().getName().equals(DocumentName.PAN)).findAny();
        if (optionalKYC.isPresent()) {
            KYC kyc = optionalKYC.get();
            KYCVerificationData kycVerificationDataToUpdate = panCardService.verifyData(kyc, kycVerificationData);
            kyc.setVerified(kycVerificationData.isVerified());
            try {
                kyc.setKYCVerificationData(new ObjectMapper().writeValueAsString(kycVerificationDataToUpdate));
            } catch (JsonProcessingException e) {
            }
            KYC savedKYC = kycRepository.save(kyc);
            return kycVerificationDataToUpdate;
        }
        return new KYCVerificationData();
    }

    @Override
    public void updateCustomerDetails(Long customerId, KYCVerificationData kycVerificationDataToUpdate) {
        Optional<KYCDataMatchResult> kycDataMatchResult = kycVerificationDataToUpdate.getMatchResults().stream()
                .filter(data -> data.getName().equals(PanField.NAME.getValue())).findAny();
        if (kycDataMatchResult.isPresent()) {
            String name = kycDataMatchResult.get().getValue();
            if (!StringUtils.isEmpty(name))
                updateCustomerDetails(customerId, name);
        }
    }

    @Override
    public GenerateOTPDto generateOtpToVerify(Long videoSessionId, KYCVerificationData kycVerificationData) {
        try {
            GenerateOTPDto generateOTPDto = aadharCardService.generateOtp(videoSessionId, kycVerificationData);
            return generateOTPDto;
        }catch (Exception ex){
            GenerateOTPDto generateOTPDto = new GenerateOTPDto();
            generateOTPDto.setStatus_code(500);
            generateOTPDto.setMessage("UIADI failed to send the OTP. Please try again.");
            return generateOTPDto;
        }

    }

    @Override
    public void verifyAadhar(Long videoSessionId, AadharDataValidationDto aadharDataValidationDto, String workflowStepName) {
        List<KYC> kycList = kycRepository.findAllByVideoSessionId(videoSessionId);
        Optional<KYC> kycOptional = kycList.stream().filter(kyc -> kyc.getWorkflowStep().getName().equals(workflowStepName)).findAny();
        if (kycOptional.isPresent()) {
            aadharCardService.validateOtp(videoSessionId, aadharDataValidationDto.getOtpValue(), kycOptional.get());
        }
    }

    @Override
    public KYCVerificationData verifyAadharData(Long videoSessionId) {

        List<KYC> kycList = kycRepository.findAllByVideoSessionId(videoSessionId);
        Optional<KYC> optionalKYC = kycList.stream().filter(kyc -> kyc.getDocument().getName().equals(DocumentName.AADHAR)).findAny();
        if (optionalKYC.isPresent()) {
            KYC kyc = optionalKYC.get();
            KYCVerificationData kycVerificationData = aadharCardService.verifyData(videoSessionId);
            kyc.setVerified(kycVerificationData.isVerified());
            try {
                kyc.setKYCVerificationData(new ObjectMapper().writeValueAsString(kycVerificationData));
            } catch (JsonProcessingException e) {
            }
            KYC savedKYC = kycRepository.save(kyc);
            return kycVerificationData;
        }
        return new KYCVerificationData();
    }

    private KYCDto prepareResponse(KYC savedKYC, KYCVerificationData kycVerificationData) {
        KYCDto savedKYCDto = KYCMapper.MAPPER.ToKYCDto(savedKYC);
        savedKYCDto.setVideoSessionId(savedKYC.getVideoSession().getId());
        savedKYCDto.setKycVerificationData(kycVerificationData);
        return savedKYCDto;
    }

    private KYC prepareKYC(Long orgId, Long videoSessionId, String workflowName, String
            workflowStepName, DocumentName documentName) {
        List<KYC> kycList = kycRepository.findAllByVideoSessionId(videoSessionId);
        List<KYC> filteredKycList = kycList.stream().filter(kyc -> kyc.getWorkflowStep().getName().equals(workflowStepName))
                .collect(Collectors.toList());
        if (filteredKycList.size() == 0) {
            KYC kyc = new KYC();

            Optional<WorkflowStep> workflowStep = workflowService.getWorkflowStep(orgId, workflowName, workflowStepName);
            if (workflowStep.isPresent())
                kyc.setWorkflowStep(workflowStep.get());
            else
                throw new IllegalArgumentException("Invalid workflow step");

            if (kyc.getWorkflowStep().getWorkflowStepType() == WorkflowStepType.DOCUMENT) {
                Optional<Document> documentByName = documentService.getDocumentByName(orgId, documentName);
                if (documentByName.isPresent())
                    kyc.setDocument(documentByName.get());
                else
                    throw new IllegalArgumentException("Invalid workflow Document");
            }

            VideoSession videoSession = new VideoSession();
            videoSession.setId(videoSessionId);
            kyc.setVideoSession(videoSession);
            return kyc;
        } else {
            return filteredKycList.get(0);
        }
    }

    private void saveResources(KYCRequestImageDto kycRequestImageDto, KYC kyc) {
        if (kyc.getWorkflowStep().getWorkflowStepType().equals(WorkflowStepType.OTHER)) {
            switch (ai.obvs.Enums.WorkflowStep.valueOf(kyc.getWorkflowStep().getName())) {
                case FACE:
                    saveResources(kycRequestImageDto.getFrontImage(), kyc, KYCResourceType.FACE);
                    break;
                case SIGNATURE:
                    saveResources(kycRequestImageDto.getFrontImage(), kyc, KYCResourceType.SIGN);
                    break;
            }
        } else {
            saveResourcesBasedOnDocument(kycRequestImageDto, kyc);
        }
    }

    private void saveResourcesBasedOnDocument(KYCRequestImageDto kycRequestImageDto, KYC kyc) {
        switch (kyc.getDocument().getName()) {
            case PAN:
                saveResources(kycRequestImageDto.getFrontImage(), kyc, KYCResourceType.PAN);
                break;
            case AADHAR:
                saveResources(kycRequestImageDto.getFrontImage(), kyc, KYCResourceType.AADHAR_FRONT);
                saveResources(kycRequestImageDto.getBackImage(), kyc, KYCResourceType.AADHAR_BACK);
                break;
        }
    }

    private void saveResources(byte[] content, KYC kyc, KYCResourceType kycResourceType) {
        Set<KYCResource> kycResources = kyc.getResources();
        if (kycResources == null) {
            prepareKYCResources(content, kyc, kycResourceType);
        } else {
            Set<KYCResource> resources = kycResources.stream().filter(kycResource -> kycResource.getKycResourceType().equals(kycResourceType)).collect(Collectors.toSet());
            if (resources.isEmpty()) {
                prepareKYCResources(content, kyc, kycResourceType);
            } else {
                resources.stream().forEach(resource ->
                        resource.setContent(content));
                resourceRepository.saveAll(resources);
//                kyc.setResources(resources);
            }
        }
    }

    private void prepareKYCResources(byte[] content, KYC kyc, KYCResourceType kycResourceType) {
        Set<KYCResource> kycResources = kyc.getResources();
        if (kycResources == null)
            kycResources = new HashSet<>();
        KYCResource kycResource = new KYCResource();
        kycResource.setKycResourceType(kycResourceType);
        kycResource.setContent(content);
        kycResources.add(kycResource);
        resourceRepository.saveAll(kycResources);
        kyc.setResources(kycResources);
    }

}
