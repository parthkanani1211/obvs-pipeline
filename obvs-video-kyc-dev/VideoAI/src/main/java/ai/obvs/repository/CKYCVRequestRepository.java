package ai.obvs.repository;

import ai.obvs.model.CKYCProfileData;
import ai.obvs.model.CKYCProfileVKYCRequest;
import ai.obvs.model.CKYCVRequest;
import ai.obvs.model.VideoKYC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CKYCVRequestRepository extends JpaRepository<CKYCVRequest, Long> {
    List<CKYCVRequest> findAllByRefIdOrderByIdDesc(String refId);
    Optional<CKYCVRequest> findByVideoKYC(VideoKYC videoKYC);
}
