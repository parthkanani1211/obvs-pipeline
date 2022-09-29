package ai.obvs.repository;

import ai.obvs.model.KYC;
import ai.obvs.model.KYCResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<KYCResource, Long> {
}
