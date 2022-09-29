package ai.obvs.repository;

import ai.obvs.model.CKYCProfileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CKYCProfileDataRepository extends JpaRepository<CKYCProfileData, Long> {
    List<CKYCProfileData> findAllByCustomerIdOrderByIdDesc(Long customerId);
    List<CKYCProfileData> findAllByRefIdOrderByIdDesc(String refId);
}
