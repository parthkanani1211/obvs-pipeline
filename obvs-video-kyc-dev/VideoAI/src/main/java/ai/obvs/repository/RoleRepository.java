package ai.obvs.repository;

import ai.obvs.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String roleName);
    List<Role> findAllByNameIn(List<String> roleNameList);

    Boolean existsByName(String roleName);
}
