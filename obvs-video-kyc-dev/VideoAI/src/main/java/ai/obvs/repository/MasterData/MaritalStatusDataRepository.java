package ai.obvs.repository.MasterData;

import ai.obvs.model.MasterData.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaritalStatusDataRepository extends JpaRepository<MaritalStatus, Long>{
    Optional<MaritalStatus> findByCode(String code);
}


