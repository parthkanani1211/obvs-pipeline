package ai.obvs.repository.MasterData;

import ai.obvs.model.MasterData.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OccupationDataRepository extends JpaRepository<Occupation, Long>{
    Optional<Occupation> findByCode(String code);

}


