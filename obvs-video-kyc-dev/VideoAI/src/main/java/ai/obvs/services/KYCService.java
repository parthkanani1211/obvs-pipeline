package ai.obvs.services;

import ai.obvs.Enums.DocumentName;
import ai.obvs.Enums.KYCResourceType;
import ai.obvs.dto.Aadhar.AadharDataValidationDto;
import ai.obvs.dto.Aadhar.AadharVerificationData;
import ai.obvs.dto.Aadhar.GenerateOTPDto;
import ai.obvs.dto.KYCDto;
import ai.obvs.dto.KYCRequestImageDto;
import ai.obvs.dto.KYCResourcesDto;
import ai.obvs.model.KYC;
import ai.obvs.model.KYCResource;
import ai.obvs.dto.KYCVerificationData;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface KYCService {

    byte[] getContent(Long kycId);

    byte[] getContent(Long kycId, KYCResourceType kycResourceType);

    KYCDto getKYC(Long videoSessionId, String workflowStep);

    List<KYCDto> getKYCDtoByVideoSessionId(Long videoSessionId);

    List<KYC> getKYCByVideoSessionId(Long videoSessionId);

    List<KYCResourcesDto> getKYCResourcesByVideoSessionId(Long videoSessionId);

    KYCDto extractAndSave(Long orgId, Long videoSessionId, Long customerId, String workflowName, String workflowStep,
                          DocumentName DocumentName, KYCRequestImageDto kycRequestImageDto, List<KYC> kycList);

    KYCVerificationData verifyPan(Long videoSessionId, KYCVerificationData kycVerificationData);

    void updateCustomerDetails(Long customerId, KYCVerificationData kycVerificationDataToUpdate);

    GenerateOTPDto generateOtpToVerify(Long videoSessionId, KYCVerificationData kycVerificationData);

    void verifyAadhar(Long videoSessionId, AadharDataValidationDto aadharDataValidationDto, String workflowStepName);

    KYCVerificationData verifyAadharData(Long videoSessionId);
}
