package ai.obvs.repository;

import ai.obvs.model.CBRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CBRequestsRepository extends JpaRepository<CBRequest, Long> {
    List<CBRequest> findAllCbRequestsByMobileNumberOrderByIdDesc(Long mobileNumber);
    List<CBRequest> findAllCbRequestByRefIdOrderByIdDesc(String refId);
}
