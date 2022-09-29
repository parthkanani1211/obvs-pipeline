package ai.obvs.repository;

import ai.obvs.model.Customer;
import ai.obvs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMobileNumber(Long mobileNumber);

    @Query("select u from User u join u.organizations o join u.roles r where o.id=:orgId and r.id=:roleId")
    List<User> findByOrganizationAndRole(@Param("orgId") Long orgId, @Param("roleId") Long roleId);

    @Query("select u from User u join u.organizations o where o.id=:orgId and u.active=1 order by u.id desc")
    List<User>  findActiveUserByOrganization(@Param("orgId") Long orgId);
}
