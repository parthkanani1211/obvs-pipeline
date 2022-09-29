package ai.obvs.services;

import ai.obvs.Enums.VideoKYCRequestStatus;
import ai.obvs.dto.*;
import ai.obvs.model.*;

import java.util.List;
import java.util.Optional;

public interface VideoKYCRequestsService {
//    void create(Long requestedBy);

    Optional<VideoKYC> getVideoKYC(Long videoKycId);

    VideoKYCRequestBaseDto create(Long orgId, Long createdBy, VKYCRequestDto vkycRequestDto);

    VideoKYCRequestBaseDto restart(Long videoKycId, VideoKYCRequestDto VideoKYCRequestDto);

    VideoKYCRequestBaseDto restart(Long videoKycId, Long userId);

    void cancelVKYCRequest(Long videoKycId, Long userId, VideoKYCRequestStatusDto videoKYCRequestStatusDto, VideoKYCRequestStatus videoKYCRequestStatus);

    VideoSessionDto update(Long id, Long servedBy);

    void joinSession(Long videoKycId, Long userId);

    void leaveSession(Long videoKycId, Long userId);

    void endSession(Long videoKycId, Long userId);

    void joinSession(Long videoKycId, String sessionName, Long userId);

    void leaveSession(Long videoKycId, String sessionName, Long userId);

    void endSession(Long videoKycId, String sessionName);

    String updateUserLocation(Long videoKycId, LocationRequestDto locationRequestDto);

    Optional<AuditResponseDto> sendRequestForAudit(Long id, Long userId, VideoKYCRequestStatusDto videoKYCRequestStatusDto);

    void rejectVKYCRequest(Long id, Long userId, VideoKYCRequestStatusDto videoKYCRequestStatusDto);

    void approveVKYCRequest(Long id, Long userId, VideoKYCRequestStatusDto videoKYCRequestStatusDto);

    void submitVKYCRequest(Long id, Long userId, VideoKYCRequestStatusDto videoKYCRequestStatusDto);

    Optional<VideoSession> findActiveSession(Long videoKycId);

    Optional<VideoSession> findLastCompletedSession(Long videoKycId);

    List<VKYCResponseDto> getAllRequests(Long orgId, User user);

    List<VKYCResponseDto> getAllRequestsByStatus(Long orgId, User user, String videoKYCRequestStatusValue);

    List<VKYCResponseDto> getRequestsByUserId(Long userId);

    List<VKYCResponseDto> getRequestsByCustomerId(Long orgId, Long userId);

    List<KYCDto> getKYCByVideoSession(String session);

    List<KYCDto> getKYCDataList(Long id);

    List<KYC> getKYCList(Long id);

    List<WorkflowStep> getCompletedKYCSteps(Long id);

    KYCAuditDto getKYCAuditData(Long id);

    Customer getCustomerDataByVideoKYCRequestId(Long id);

    KYCDto getKYCData(Long id, String kycType);

    Optional<AuditResponseDto> saveAudit(AuditRequestDto auditRequestDto);

    AuditDataDto getAuditDetail(Long auditId);

    VKYCResponseDto getRequestInitiatedByMobileNumber(Long orgId, Long mobileNumber);

    List<VKYCResponseDto> getRequestsInitiatedByMobileNumber(Long orgId, Long mobileNumber);

    VideoKYC getVideoKYCRequestByMobileNumber(Long orgId, Long mobileNumber);
}
