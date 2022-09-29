package ai.obvs.repository.MasterData;

import ai.obvs.model.MasterData.Constitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConstitutionDataRepository extends JpaRepository<Constitution, Long>{
    Optional<Constitution> findByCode(String code);

}


