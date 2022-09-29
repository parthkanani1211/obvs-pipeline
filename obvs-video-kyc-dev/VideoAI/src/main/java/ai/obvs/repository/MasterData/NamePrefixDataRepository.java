package ai.obvs.repository.MasterData;

import ai.obvs.model.MasterData.NamePrefix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NamePrefixDataRepository extends JpaRepository<NamePrefix, Long>{
    Optional<NamePrefix> findByCode(String code);

}


