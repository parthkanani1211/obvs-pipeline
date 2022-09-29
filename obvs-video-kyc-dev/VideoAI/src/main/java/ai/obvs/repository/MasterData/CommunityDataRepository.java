package ai.obvs.repository.MasterData;

import ai.obvs.model.MasterData.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityDataRepository extends JpaRepository<Community, Long>{
    Optional<Community> findByCode(String code);
}


