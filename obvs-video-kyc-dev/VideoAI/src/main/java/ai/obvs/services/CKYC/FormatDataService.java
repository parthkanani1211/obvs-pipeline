package ai.obvs.services.CKYC;

import ai.obvs.model.CBRequest;
import ai.obvs.model.CKYCProfileData;
import ai.obvs.model.VideoKYC;
import ai.obvs.services.CKYC.model.CKYCResponseData;

public interface FormatDataService {
    CKYCResponseData getCKYCData(CBRequest cbRequest, VideoKYC videoKYC, CKYCProfileData ckycProfileData);
}
