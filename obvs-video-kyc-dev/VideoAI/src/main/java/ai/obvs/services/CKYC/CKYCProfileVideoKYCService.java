package ai.obvs.services.CKYC;

import ai.obvs.model.CKYCProfileData;
import ai.obvs.model.CKYCProfileVKYCRequest;
import ai.obvs.model.CKYCVRequest;
import ai.obvs.model.VideoKYC;

import java.util.List;
import java.util.Optional;

public interface CKYCProfileVideoKYCService {
    CKYCProfileVKYCRequest save(CKYCProfileVKYCRequest ckycProfileVKYCRequest);

    Optional<CKYCProfileVKYCRequest> findByCKYCVRequest(CKYCVRequest ckycvRequest);

    CKYCVRequest save(CKYCVRequest ckycvRequest);

    CKYCVRequest save(Long videoKYCId, String refId);

    List<CKYCProfileVKYCRequest> findAll(CKYCProfileData ckycProfileData);

    List<CKYCVRequest> findAllByRefId(String refId);

    Optional<CKYCVRequest> findByVideoKYC(VideoKYC videoKYC);
}
