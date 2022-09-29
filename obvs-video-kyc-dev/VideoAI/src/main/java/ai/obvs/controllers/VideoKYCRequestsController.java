package ai.obvs.controllers;

import ai.obvs.Enums.*;
import ai.obvs.dto.Aadhar.AadharDataValidationDto;
import ai.obvs.dto.*;
import ai.obvs.dto.Aadhar.GenerateOTPDto;
import ai.obvs.dto.workflow.WorkflowStepDto;
import ai.obvs.mapper.WorkflowStepMapper;
import ai.obvs.model.*;
import ai.obvs.security.CurrentUser;
import ai.obvs.security.UserPrincipal;
import ai.obvs.services.*;
import ai.obvs.services.CKYC.CKYCDataService;
import ai.obvs.services.CKYC.CKYCProfileVideoKYCService;
import ai.obvs.services.notification.NotificationService;
import ai.obvs.services.notification.SMSService;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequestMapping("/v1/videoKYC/requests")
public class VideoKYCRequestsController {

    private VideoKYCRequestsService videoKYCRequestsService;
    private KYCService kycService;
    private FileStorageService fileStorageService;
    private SMSService smsService;
    private VideoKYCRequestsRecordingService videoKYCRequestsRecordingService;

    @Autowired
    private CKYCProfileVideoKYCService ckycProfileVideoKYCService;

    @Autowired
    private CKYCDataService ckycDataService;

    @Autowired
    private NotificationService notificationService;

    public VideoKYCRequestsController(VideoKYCRequestsService videoKYCRequestsService, KYCService kycService,
                                      FileStorageService fileStorageService, SMSService smsService,
                                      VideoKYCRequestsRecordingService videoKYCRequestsRecordingService) {
        this.videoKYCRequestsService = videoKYCRequestsService;
        this.kycService = kycService;
        this.fileStorageService = fileStorageService;
        this.smsService = smsService;
        this.videoKYCRequestsRecordingService = videoKYCRequestsRecordingService;
    }

//    @PostMapping("/")
//    public ResponseEntity<?> initiateRequest(@Valid @RequestBody VideoKYCRequestDto videoKYCRequestDto) {
//        videoKYCRequestsService.create(videoKYCRequestDto.getUserId());
//        return ResponseEntity.ok("");
//    }

    @PostMapping("/")
    public ResponseEntity<?> initiateRequest(@RequestHeader("x-obvs-org") Long orgId,
                                             @Valid @RequestBody VKYCRequestDto vkycRequestDto,
                                             @CurrentUser UserPrincipal currentUser) {
        VideoKYCRequestBaseDto videoKYCRequestBaseDto = videoKYCRequestsService.create(orgId, currentUser.getUser().getId(), vkycRequestDto);
        saveVideoKYCForRefId(vkycRequestDto.getRefId(), videoKYCRequestBaseDto.getId());
        return ResponseEntity.ok(videoKYCRequestBaseDto);
    }

//    @PutMapping("{id}/restart")
//    public ResponseEntity<?> restartRequest(@PathVariable(value = "id") Long id, @Valid @RequestBody VideoKYCRequestDto videoKYCRequestDto) {
//        VideoKYCRequestBaseDto videoKYCRequestBaseDto = videoKYCRequestsService.restart(id, videoKYCRequestDto);
//        return ResponseEntity.ok(videoKYCRequestBaseDto);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<?> processRequest(@PathVariable(value = "id") Long id,
                                            @Valid @RequestBody VideoKYCRequestDto videoKYCRequestDto,
                                            @CurrentUser UserPrincipal currentUser) {

        String action = videoKYCRequestDto.getAction();
        Long userId = videoKYCRequestDto.getUserId();
        if (userId == null) {
            userId = currentUser.getUser().getId();
        }

        VideoKYCRequestStatusDto videoKYCRequestStatusDto = new VideoKYCRequestStatusDto();
        videoKYCRequestStatusDto.setComment(videoKYCRequestDto.getComment());

        try {
            VideoKYCRequestAction videoKYCRequestAction = VideoKYCRequestAction.valueOf(action.toUpperCase());
            switch (videoKYCRequestAction) {
                case RESTART:
                    VideoKYCRequestBaseDto videoKYCRequestBaseDto = videoKYCRequestsService.restart(id, videoKYCRequestDto);
                    return ResponseEntity.ok(videoKYCRequestBaseDto);
                case COMPLETE:
                    Optional<AuditResponseDto> auditResponseDto = videoKYCRequestsService.sendRequestForAudit(id, userId, videoKYCRequestStatusDto);
                    return auditResponseDto.isPresent() ? ResponseEntity.ok(auditResponseDto.get()) : ResponseEntity.ok("{}");
                case SUBMIT:
                    videoKYCRequestsService.submitVKYCRequest(id, userId, videoKYCRequestStatusDto);
                    return ResponseEntity.ok("{}");
                case APPROVE:
                    videoKYCRequestsService.approveVKYCRequest(id, userId, videoKYCRequestStatusDto);
                    updateCustomerProfileStatus(id);
                    return ResponseEntity.ok("{}");
                case REJECT:
                    videoKYCRequestsService.rejectVKYCRequest(id, userId, videoKYCRequestStatusDto);
                    return ResponseEntity.ok("{}");
                case CANCEL:
                    videoKYCRequestsService.cancelVKYCRequest(id, userId, videoKYCRequestStatusDto, VideoKYCRequestStatus.CANCELED);
                    return ResponseEntity.ok("{}");
                case TIMEOUT:
                    videoKYCRequestsService.cancelVKYCRequest(id, userId, videoKYCRequestStatusDto, VideoKYCRequestStatus.TIMEOUT);
                    return ResponseEntity.ok("{}");
                default:
            }

        } catch (Exception ex) {
        }

        processVideoSessionRequests(id, action, userId);
        return ResponseEntity.ok("{}");
    }

    private void processVideoSessionRequests(Long id, String action, Long userId) {
        VideoSessionAction videoSessionAction = VideoSessionAction.valueOf(action.toUpperCase());
        switch (videoSessionAction) {
            case JOIN:
                videoKYCRequestsService.joinSession(id, userId);
                break;
            case LEAVE:
                videoKYCRequestsService.leaveSession(id, userId);
                break;
            case END:
                videoKYCRequestsService.endSession(id, userId);
                break;
        }
    }

    @PutMapping("/{id}/{action}")
    public ResponseEntity<?> processRequest(@PathVariable(value = "id") Long id, @PathVariable(value = "action") String action,
                                            @Valid @RequestBody VideoKYCRequestStatusDto videoKYCRequestStatusDto, @CurrentUser UserPrincipal userPrincipal) {

        Long userId = userPrincipal.getUser().getId();
        VideoKYCRequestAction videoKYCRequestAction = VideoKYCRequestAction.valueOf(action.toUpperCase());
        switch (videoKYCRequestAction) {
            case RESTART:
                VideoKYCRequestBaseDto videoKYCRequestBaseDto = videoKYCRequestsService.restart(id, userId);
                return ResponseEntity.ok(videoKYCRequestBaseDto);
            case COMPLETE:
                Optional<AuditResponseDto> auditResponseDto = videoKYCRequestsService.sendRequestForAudit(id, userId, videoKYCRequestStatusDto);
                return auditResponseDto.isPresent() ? ResponseEntity.ok(auditResponseDto.get()) : ResponseEntity.ok("{}");
            case SUBMIT:
                videoKYCRequestsService.submitVKYCRequest(id, userId, videoKYCRequestStatusDto);
                return ResponseEntity.ok("{}");
            case APPROVE:
                videoKYCRequestsService.approveVKYCRequest(id, userId, videoKYCRequestStatusDto);
                updateCustomerProfileStatus(id);
                return ResponseEntity.ok("{}");
            case REJECT:
                videoKYCRequestsService.rejectVKYCRequest(id, userId, videoKYCRequestStatusDto);
                return ResponseEntity.ok("{}");
            case CANCEL:
                videoKYCRequestsService.cancelVKYCRequest(id, userId, videoKYCRequestStatusDto, VideoKYCRequestStatus.CANCELED);
                return ResponseEntity.ok("{}");
            case TIMEOUT:
                videoKYCRequestsService.cancelVKYCRequest(id, userId, videoKYCRequestStatusDto, VideoKYCRequestStatus.TIMEOUT);
                return ResponseEntity.ok("{}");
            default:
                throw new IllegalArgumentException("Unexpected value: " + videoKYCRequestAction);
        }
    }

    private void updateCustomerProfileStatus(Long id) {
        try {
            Optional<VideoKYC> videoKYCOptional = videoKYCRequestsService.getVideoKYC(id);
            if (videoKYCOptional.isPresent()) {
                Optional<CKYCVRequest> byVideoKYC = ckycProfileVideoKYCService.findByVideoKYC(videoKYCOptional.get());
                if(byVideoKYC.isPresent()){
                    CKYCVRequest ckycvRequest = byVideoKYC.get();
                    CKYCProfileData ckycProfileData = ckycDataService.findCKYCProfileDataByRefId(ckycvRequest.getRefId());
                    CKYCProfileVKYCRequest ckycProfileVKYCRequest = new CKYCProfileVKYCRequest();
                    ckycProfileVKYCRequest.setCkycProfileData(ckycProfileData);
                    ckycProfileVKYCRequest.setCkycvRequest(ckycvRequest);
                    ckycProfileVideoKYCService.save(ckycProfileVKYCRequest);
                }
            }
        } catch (Exception ex) {
        }
    }

    private void saveVideoKYCForRefId(String refId, Long videoKYCId) {
        ckycProfileVideoKYCService.save(videoKYCId, refId);
    }

    @PutMapping("/{id}/session/{session}/{action}")
    public ResponseEntity<?> updateVideoSession(@PathVariable(value = "id") Long id, @PathVariable(value = "action") String action,
                                                @PathVariable(value = "session") String sessionName,
                                                @CurrentUser UserPrincipal userPrincipal) {

        VideoSessionAction videoSessionAction = VideoSessionAction.valueOf(action.toUpperCase());
        Long userId = userPrincipal.getUser().getId();
        switch (videoSessionAction) {
            case JOIN:
                videoKYCRequestsService.joinSession(id, sessionName, userId);
                return ResponseEntity.ok("{}");
            case LEAVE:
                videoKYCRequestsService.leaveSession(id, sessionName, userId);
                return ResponseEntity.ok("{}");
            case END:
                videoKYCRequestsService.endSession(id, sessionName);
                return ResponseEntity.ok("{}");
            default:
                throw new IllegalArgumentException("Unexpected value: " + videoSessionAction);
        }

    }

    @GetMapping("/")
    public ResponseEntity<?> getRequestsForUser(@RequestHeader("x-obvs-org") Long orgId, @CurrentUser UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUser().getId();
        List<VKYCResponseDto> videoKYCRequests = videoKYCRequestsService.getRequestsByCustomerId(orgId, userId);
        return ResponseEntity.ok(videoKYCRequests);
    }

    @GetMapping("/{id}/kyc/all")
    public ResponseEntity<?> getAllKYCData(@PathVariable(value = "id") Long id) {
        List<KYCDto> kycDtoList = videoKYCRequestsService.getKYCDataList(id);
        return ResponseEntity.ok(kycDtoList);
    }

    @GetMapping("/{id}/kyc/completed")
    public ResponseEntity<?> getCompletedKYCTypes(@PathVariable(value = "id") Long id) {
        List<ai.obvs.model.WorkflowStep> completedKYCSteps = videoKYCRequestsService.getCompletedKYCSteps(id);
        List<WorkflowStepDto> workflowStepDtos = WorkflowStepMapper.MAPPER.ToWorkflowStepDtoList(completedKYCSteps);
        List<KYCType> kycTypes = new ArrayList<>();
        if (workflowStepDtos.size() == 1)
            kycTypes.add(KYCType.PAN);
        if (workflowStepDtos.size() == 2) {
            kycTypes.add(KYCType.PAN);
            kycTypes.add(KYCType.AADHAR);
        }
        if (workflowStepDtos.size() == 3) {
            kycTypes.add(KYCType.PAN);
            kycTypes.add(KYCType.AADHAR);
            kycTypes.add(KYCType.FACE);
        }
        if (workflowStepDtos.size() == 4) {
            kycTypes.add(KYCType.PAN);
            kycTypes.add(KYCType.AADHAR);
            kycTypes.add(KYCType.FACE);
            kycTypes.add(KYCType.SIGN);
        }

        return ResponseEntity.ok(kycTypes);
//        return ResponseEntity.ok(workflowStepDtos);
    }

    @GetMapping("/{id}/kyc/{step}")
    public ResponseEntity<?> getKYCData(@PathVariable(value = "id") Long id, @PathVariable(value = "step") String step) {
        KYCDto kycDto = videoKYCRequestsService.getKYCData(id, step);
        return ResponseEntity.ok(kycDto);
    }

    @GetMapping("/{id}/{type}/image")
    public ResponseEntity<?> getKYCData(@PathVariable(value = "id") Long id) {
        byte[] content = kycService.getContent(id);
        return ResponseEntity.ok(new ByteArrayResource(content));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllRequests(@RequestHeader("x-obvs-org") Long orgId, @CurrentUser UserPrincipal currentUer) {
        List<VKYCResponseDto> activeRequests = videoKYCRequestsService.getAllRequests(orgId, currentUer.getUser());
        return ResponseEntity.ok(activeRequests);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveRequests(@RequestHeader("x-obvs-org") Long orgId, @CurrentUser UserPrincipal currentUer) {
        List<VKYCResponseDto> activeRequests = videoKYCRequestsService.getAllRequests(orgId, currentUer.getUser());
        return ResponseEntity.ok(activeRequests);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getAllRequestsByStatus(@RequestHeader("x-obvs-org") Long orgId, @CurrentUser UserPrincipal currentUer,
                                                    @PathVariable(value = "status") String status) {
        List<VKYCResponseDto> activeRequests = videoKYCRequestsService.getAllRequestsByStatus(orgId, currentUer.getUser(), status);
        return ResponseEntity.ok(activeRequests);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> extractKYCData(@RequestHeader("x-obvs-org") Long orgId,
                                            @PathVariable(value = "id") Long id,
                                            @RequestParam(value = "kycType") String kycType,
//                                       @RequestParam(value = "workflowName") String workflowName,
//                                       @RequestParam(value = "workflowStep") String workflowStep,
//                                       @RequestParam(value = "documentName") DocumentName DocumentName,
                                            @RequestParam("files") MultipartFile[] files) throws IOException {

        String workflowName = "videoKYC";

        WorkflowStepType workflowStepType = WorkflowStepType.OTHER;
        String workflowStep = kycType;
        DocumentName documentName = DocumentName.PAN;
        try {
            documentName = DocumentName.valueOf(kycType);
            switch (documentName) {
                case PAN:
                    workflowStepType = WorkflowStepType.DOCUMENT;
                    break;
                case AADHAR:
                    workflowStepType = WorkflowStepType.DOCUMENT;
                    break;
            }
        } catch (Exception exception) {
        }
        List<KYC> kycList = new ArrayList<>();
        List<MultipartFile> kycImageFiles = Arrays.asList(files);

        KYCRequestImageDto kycRequestImageDto = new KYCRequestImageDto();
        if (kycImageFiles.isEmpty()) {
            throw new RuntimeException("Image not captured properly.");
        }

        switch (workflowStepType) {
            case DOCUMENT:
                String filePrefix = "_" + workflowStep + "_" + documentName + "_";
                switch (documentName) {
                    case PAN:
                        MultipartFile multipartFile = kycImageFiles.get(0);
                        byte[] multipartFileBytes = multipartFile.getBytes();
                        if (multipartFileBytes != null) {
                            String fileName = id + filePrefix + DateTime.now().getSecondOfDay();
                            String imagePath = fileStorageService.storeImage("input/images", fileName + ".png", multipartFileBytes);
                            kycRequestImageDto.setFrontImage(multipartFile.getBytes());
                            kycRequestImageDto.setFrontImagePath(imagePath);
                        } else
                            throw new RuntimeException("Image not captured properly.");
                        break;
                    case AADHAR:
                        if (kycImageFiles.size() < 2) {
                            throw new RuntimeException("Image not captured properly.");
                        }
                        MultipartFile frontImageFile = kycImageFiles.get(0);
                        MultipartFile backImageFile = kycImageFiles.get(1);
                        kycRequestImageDto.setFrontImage(frontImageFile.getBytes());
                        kycRequestImageDto.setBackImage(backImageFile.getBytes());

                        byte[] frontFileBytes = frontImageFile.getBytes();
                        byte[] backFileBytes = backImageFile.getBytes();
                        if (frontFileBytes != null || backFileBytes != null) {
                            //<customername>_<imagetype:pan/aadhar/signature/face>_<timestamp:yyyymmdd>_input.png
                            String fileName = id + filePrefix + "1_" + DateTime.now().getSecondOfDay();
                            String imagePath1 = fileStorageService.storeImage("input/images", fileName + ".png", frontFileBytes);
                            fileName = id + filePrefix + "2_" + DateTime.now().getSecondOfDay();
                            String imagePath2 = fileStorageService.storeImage("input/images", fileName + ".png", backFileBytes);
                            kycRequestImageDto.setFrontImage(frontFileBytes);
                            kycRequestImageDto.setBackImage(backFileBytes);
                            kycRequestImageDto.setFrontImagePath(imagePath1);
                            kycRequestImageDto.setBackImagePath(imagePath2);
                        } else
                            throw new RuntimeException("Image not captured properly.");
                        break;
                }
                break;
            case OTHER:
                filePrefix = "_" + workflowStep + "_";
                switch (ai.obvs.Enums.WorkflowStep.EnumOfString(workflowStep)) {
                    case SIGNATURE:
                        workflowStep = ai.obvs.Enums.WorkflowStep.SIGNATURE.toString();
                        MultipartFile signatureFile = kycImageFiles.get(0);
                        byte[] signatureFileBytes = signatureFile.getBytes();
                        if (signatureFileBytes != null) {
                            String fileName = id + filePrefix + DateTime.now().getSecondOfDay();
                            String imagePath = fileStorageService.storeImage("input/images", fileName + ".png", signatureFileBytes);
                            kycRequestImageDto.setFrontImage(signatureFileBytes);
                            kycRequestImageDto.setFrontImagePath(imagePath);
                        } else
                            throw new RuntimeException("Image not captured properly.");
                        break;
                    case FACE:
                        MultipartFile faceFile1 = kycImageFiles.get(0);
                        MultipartFile faceFile2 = kycImageFiles.get(1);
                        byte[] faceFileBytes1 = faceFile1.getBytes();
                        byte[] faceFileBytes2 = faceFile2.getBytes();
                        if (faceFileBytes1 != null && faceFileBytes2 != null) {
                            String fileName1 = id + "_FACE_1_" + DateTime.now().getSecondOfDay();
                            String imagePath1 = fileStorageService.storeImage("input/images", fileName1 + ".png", faceFileBytes1);
                            kycRequestImageDto.setFrontImage(faceFileBytes1);
                            kycRequestImageDto.setFrontImagePath(imagePath1);

                            String fileName2 = id + "_FACE_2_" + DateTime.now().getSecondOfDay();
                            String imagePath2 = fileStorageService.storeImage("input/images", fileName2 + ".png", faceFileBytes2);
                            kycRequestImageDto.setBackImage(faceFileBytes2);
                            kycRequestImageDto.setBackImagePath(imagePath2);
                        } else
                            throw new RuntimeException("Image not captured properly.");

                        kycList = videoKYCRequestsService.getKYCList(id);
                        break;
                }
                break;
        }
        Optional<VideoKYC> videoKYCOptional = videoKYCRequestsService.getVideoKYC(id);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();
            Long customerId = videoKYC.getCustomer().getId();
            Optional<VideoSession> activeSession = videoKYCRequestsService.findActiveSession(id);
            if (activeSession.isPresent()) {
                KYCDto savedKYCDto = kycService.extractAndSave(orgId, activeSession.get().getId(), customerId, workflowName, workflowStep,
                        documentName, kycRequestImageDto, kycList);
                savedKYCDto.setKycType(KYCType.valueOf(kycType));

                if (kycRequestImageDto.getFrontImagePath() != null)
                    Files.deleteIfExists(Paths.get(kycRequestImageDto.getFrontImagePath()));
                if (kycRequestImageDto.getBackImagePath() != null)
                    Files.deleteIfExists(Paths.get(kycRequestImageDto.getBackImagePath()));

                return ResponseEntity.ok(savedKYCDto);
            }
        }
        throw new IllegalArgumentException("Invalid input params.");
    }

    @PostMapping("/{id}/kyc/pan/verify")
    public ResponseEntity<?> verifyPan(@PathVariable(value = "id") Long id, @RequestBody KYCVerificationData kycVerificationData) {
        try {
            Optional<VideoSession> activeSession = videoKYCRequestsService.findActiveSession(id);

            if (activeSession.isPresent()) {
                KYCVerificationData verificationData = kycService.verifyPan(activeSession.get().getId(), kycVerificationData);
                Optional<VideoKYC> videoKYCOptional = videoKYCRequestsService.getVideoKYC(id);
                if (videoKYCOptional.isPresent()) {
                    VideoKYC videoKYC = videoKYCOptional.get();
                    Long customerId = videoKYC.getCustomer().getId();
                    kycService.updateCustomerDetails(customerId, kycVerificationData);
                }
                return ResponseEntity.ok(verificationData);
            }
            throw new IllegalArgumentException("Invalid session");
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @PostMapping("/{id}/kyc/aadhar/verify")
    public ResponseEntity<?> verifyAadhar(@PathVariable(value = "id") Long id, @RequestBody KYCVerificationData kycVerificationData) {
        Optional<VideoSession> activeSession = videoKYCRequestsService.findActiveSession(id);
        if (activeSession.isPresent()) {
            GenerateOTPDto generateOTPDto = kycService.generateOtpToVerify(activeSession.get().getId(), kycVerificationData);
            return ResponseEntity.ok(generateOTPDto);
        }
        throw new IllegalArgumentException("Invalid session");
    }

    @PostMapping("/{id}/kyc/aadhar/validate-otp")
    public ResponseEntity<?> validateOtp(@PathVariable(value = "id") Long id, @RequestBody AadharDataValidationDto aadharDataValidationDto) {

        Optional<VideoSession> activeSession = videoKYCRequestsService.findActiveSession(id);
        String workflowStepName = "AADHAR";
        if (activeSession.isPresent()) {
            kycService.verifyAadhar(activeSession.get().getId(), aadharDataValidationDto, workflowStepName);
            return ResponseEntity.ok("");
        }
        throw new IllegalArgumentException("Invalid session");
    }

    @GetMapping("/{id}/kyc/aadhar/verifydata")
    public ResponseEntity<?> GetAadharVerificationData(@PathVariable(value = "id") Long id) {
        Optional<VideoSession> activeSession = videoKYCRequestsService.findActiveSession(id);
        if (activeSession.isPresent()) {
            KYCVerificationData kycVerificationData = kycService.verifyAadharData(activeSession.get().getId());
            return ResponseEntity.ok(kycVerificationData);
        }
        throw new IllegalArgumentException("Invalid session");
    }

    @PostMapping("/{id}/location")
    public ResponseEntity<?> updateLocation(@PathVariable(value = "id") Long id, @Valid @RequestBody LocationRequestDto locationRequestDto) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String userLocation = videoKYCRequestsService.updateUserLocation(id, locationRequestDto);
        ResponseEntity<String> responseEntity = new ResponseEntity(userLocation, httpHeaders, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/{id}/recording/download")
    public ResponseEntity<?> downloadRecording(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currentUser) {

        Optional<VideoSession> activeSession = videoKYCRequestsService.findLastCompletedSession(id);
        if (activeSession.isPresent()) {
            try {
                VideoSession videoSession = activeSession.get();
                Response response = videoKYCRequestsRecordingService.downloadRecording(videoSession);
                if (response.getStatusCode() == HttpStatus.OK.value()) {
                    String fileName = videoSession.getSession() + ".mp4";

                    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .header("Content-Disposition", "attachment; filename=\"" + fileName + "\";")
                            .body(new ByteArrayResource(response.getContent()));
                }
//                return ResponseEntity.ok(new ByteArrayResource(response.getContent()));
            } catch (OpenViduJavaClientException | OpenViduHttpException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Invalid input params.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{mobileNumber}/status")
    public ResponseEntity<?> getStatus(@RequestHeader("x-obvs-org") Long orgId, @PathVariable(value = "mobileNumber") Long mobileNumber) {
        VKYCResponseDto requestInitiatedByMobileNumber = videoKYCRequestsService.getRequestInitiatedByMobileNumber(orgId, mobileNumber);
        return ResponseEntity.ok(requestInitiatedByMobileNumber);
    }

//    @PostMapping
//    public ResponseEntity<?> verifyPhoto(){
//        // using org.json.JSONObject from JSON-java library
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("liveimage1", "data:image/png;base64," + Base64.getEncoder().encodeToString(png1AsByteArray));
//        requestBody.put("liveimage2", "data:image/png;base64," + Base64.getEncoder().encodeToString(png2AsByteArray));
//        requestBody.put("idphoto", "data:image/png;base64," + Base64.getEncoder().encodeToString(photoIdPngAsByteArray));
//
//// using OkHttpClient from the OkHttp library
//        Request request = new Request.Builder()
//                .url("https://bws.bioid.com/extension/photoverify")
//                .addHeader("Authorization", Credentials.basic(APP_IDENTIFIER, APP_SECRET))
//                .post(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()))
//                .build();
//        OkHttpClient client = new OkHttpClient();
//        Response response = client.newCall(request).execute();
//        if (response.code() == 200) {
//            if (response.body().string().equals("true")) {
//                System.out.println("live images do match the ID photo");
//            } else {
//                System.out.println("live images do not match the ID photo");
//            }
//        }
//    }
}
