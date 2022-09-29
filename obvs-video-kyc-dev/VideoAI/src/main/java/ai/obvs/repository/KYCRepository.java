package ai.obvs.repository;

import ai.obvs.Enums.VideoKYCRequestStatus;
import ai.obvs.model.KYC;
import ai.obvs.model.VideoKYC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KYCRepository extends JpaRepository<KYC, Long> {
    List<KYC> findAllByVideoSessionId(Long videoSessionId);
//    List<KYC> findAllByVideoSessionId(List<Long> videoSessionId);

}
