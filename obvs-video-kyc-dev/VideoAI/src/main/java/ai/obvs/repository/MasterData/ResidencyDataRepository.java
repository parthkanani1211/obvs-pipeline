package ai.obvs.repository.MasterData;

import ai.obvs.model.MasterData.ResidencyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResidencyDataRepository extends JpaRepository<ResidencyStatus, Long>{
    Optional<ResidencyStatus> findByCode(String code);

}


