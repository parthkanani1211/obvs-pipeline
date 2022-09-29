package ai.obvs.repository;

import ai.obvs.model.KYC;
import ai.obvs.model.KYCData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KYCDataRepository extends JpaRepository<KYCData, Long> {
Optional<KYCData> findByKycId(Long id);
}
