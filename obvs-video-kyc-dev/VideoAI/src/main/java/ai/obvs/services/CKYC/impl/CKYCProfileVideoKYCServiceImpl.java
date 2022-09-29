package ai.obvs.services.CKYC.impl;

import ai.obvs.model.CKYCProfileData;
import ai.obvs.model.CKYCProfileVKYCRequest;
import ai.obvs.model.CKYCVRequest;
import ai.obvs.model.VideoKYC;
import ai.obvs.repository.CKYCProfileVideoKYCRepository;
import ai.obvs.repository.CKYCVRequestRepository;
import ai.obvs.repository.VideoKYCRepository;
import ai.obvs.services.CKYC.CKYCProfileVideoKYCService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class CKYCProfileVideoKYCServiceImpl implements CKYCProfileVideoKYCService {

    @Autowired
    private CKYCProfileVideoKYCRepository ckycProfileVideoKYCRepository;

    @Autowired
    private CKYCVRequestRepository ckycvRequestRepository;

    @Autowired
    private VideoKYCRepository videoKYCRepository;

    @Override
    public CKYCProfileVKYCRequest save(CKYCProfileVKYCRequest ckycProfileVKYCRequest) {
        return ckycProfileVideoKYCRepository.save(ckycProfileVKYCRequest);
    }

    @Override
    public Optional<CKYCProfileVKYCRequest> findByCKYCVRequest(CKYCVRequest ckycvRequest) {
        return ckycProfileVideoKYCRepository.findByCkycvRequest(ckycvRequest);
    }

    @Override
    public CKYCVRequest save(Long videoKYCId, String refId) {
        Optional<VideoKYC> byId = videoKYCRepository.findById(videoKYCId);
        if(byId.isPresent()) {
            VideoKYC videoKYC = byId.get();
            CKYCVRequest ckycvRequest = new CKYCVRequest();
            ckycvRequest.setRefId(refId);
            ckycvRequest.setVideoKYC(videoKYC);
            return save(ckycvRequest);
        }
        return new CKYCVRequest();
    }

    @Override
    public List<CKYCProfileVKYCRequest> findAll(CKYCProfileData ckycProfileData) {
        return ckycProfileVideoKYCRepository.findAllByCkycProfileDataOrderByIdDesc(ckycProfileData);
    }

    @Override
    public CKYCVRequest save(CKYCVRequest ckycvRequest) {
        return ckycvRequestRepository.save(ckycvRequest);
    }

    @Override
    public List<CKYCVRequest> findAllByRefId(String refId) {
        return ckycvRequestRepository.findAllByRefIdOrderByIdDesc(refId);
    }

    @Override
    public Optional<CKYCVRequest> findByVideoKYC(VideoKYC videoKYC) {
        return ckycvRequestRepository.findByVideoKYC(videoKYC);
    }

}
