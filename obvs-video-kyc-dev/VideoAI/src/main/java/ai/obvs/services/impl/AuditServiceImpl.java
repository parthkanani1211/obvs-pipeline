package ai.obvs.services.impl;

import ai.obvs.dto.*;
import ai.obvs.model.Audit;
import ai.obvs.dto.KYCVerificationData;
import ai.obvs.repository.AuditRepository;
import ai.obvs.services.AuditService;
import ai.obvs.services.CustomerService;
import ai.obvs.services.VideoKYCRequestsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuditServiceImpl implements AuditService {

    private VideoKYCRequestsService videoKYCRequestsService;
    private CustomerService customerService;
    private AuditRepository auditRepository;

    public AuditServiceImpl(VideoKYCRequestsService videoKYCRequestsService, AuditRepository auditRepository){
        this.videoKYCRequestsService = videoKYCRequestsService;
        this.auditRepository = auditRepository;
    }

    @Override
    public void saveAudit(AuditRequestDto auditRequestDto) {
        Audit audit = new Audit();
        audit.setAgentId(auditRequestDto.getAgentId());
        audit.setCustomerId (auditRequestDto.getCustomerId());
        audit.setVideoKYCId (auditRequestDto.getVideoKYCId());
        audit.setRequestTime(ZonedDateTime.now());
        auditRepository.save(audit);
    }

    @Override
    public AuditDataDto getAuditDetail(Long auditId) {
        Audit audit = auditRepository.getOne(auditId);
        Long videoKYCId = audit.getVideoKYCId();
        KYCAuditDto kycAuditData = videoKYCRequestsService.getKYCAuditData(videoKYCId);
        List<KYCResourcesDto> kycDataList = kycAuditData.getKycResourcesDtoList();
        Map<String, KYCVerificationData> kycTypeKYCVerificationDataMap = new HashMap<>();

        kycDataList.forEach(kycDto -> {
            KYCVerificationData kycVerificationData = new KYCVerificationData();
            try {
                kycVerificationData = new ObjectMapper().readValue(kycDto.getKYCVerificationData(), KYCVerificationData.class);
            } catch (JsonProcessingException e) {
            }
            kycTypeKYCVerificationDataMap.put(kycDto.getWorkflowStepDto().getName(), kycVerificationData);
        });
        AuditDataDto auditDataDto = new AuditDataDto();
        auditDataDto.setKycTypeKYCVerificationDataMap(kycTypeKYCVerificationDataMap);
//        auditDataDto.setCustomerDto(kycAuditData.getCustomerDto());
        auditDataDto.setAgentDto(kycAuditData.getAgentDto());
        return auditDataDto;
    }
}
