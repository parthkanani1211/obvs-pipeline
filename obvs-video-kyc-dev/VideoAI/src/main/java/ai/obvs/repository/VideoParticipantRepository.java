package ai.obvs.repository;

import ai.obvs.model.VideoParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoParticipantRepository extends JpaRepository<VideoParticipant, Long>{

    @Query(value = "SELECT * FROM video_participants v WHERE v.user_id = :userId", nativeQuery = true)
    List<VideoParticipant> findAllByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM video_participants v WHERE v.video_session_id = :sessionId", nativeQuery = true)
    List<VideoParticipant> findAllByVideoSessionId(@Param("sessionId") Long sessionId);

    @Query(value = "SELECT * FROM video_participants v WHERE v.user_id = :userId AND v.video_session_id = :sessionId", nativeQuery = true)
    Optional<VideoParticipant> findByUserIdAndVideoSessionId(@Param("userId") Long userId, @Param("sessionId") Long sessionId);
}

