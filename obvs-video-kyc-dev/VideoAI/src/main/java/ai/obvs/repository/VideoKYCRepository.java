package ai.obvs.repository;

import ai.obvs.model.Organization;
import ai.obvs.model.VideoKYC;
import ai.obvs.Enums.VideoKYCRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoKYCRepository extends JpaRepository<VideoKYC, Long> {
    List<VideoKYC> findAllByOrganizationAndVideoKYCRequestStatusInOrderByIdDesc(Organization organization, List<VideoKYCRequestStatus> videoKYCRequestStatusList);
    List<VideoKYC> findAllByOrganizationAndVideoKYCRequestStatusOrderByIdDesc(Organization organization, VideoKYCRequestStatus videoKYCRequestStatus);
    List<VideoKYC> findAllByOrderByIdDesc();
    List<VideoKYC> findAllByOrganizationOrderByIdDesc(Organization organization);
    List<VideoKYC> findAllByCustomerIdOrderByIdDesc(Long customerId);
    List<VideoKYC> findAllByOrganizationAndCustomerIdOrderByIdDesc(Organization organization, Long customerId);
}
