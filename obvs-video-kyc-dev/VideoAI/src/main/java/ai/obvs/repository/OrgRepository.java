package ai.obvs.repository;

import ai.obvs.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrgRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByName(String name);
}
