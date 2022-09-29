package ai.obvs.repository;

import ai.obvs.model.VideoSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoSessionRepository extends JpaRepository<VideoSession, Long> {
    VideoSession findBySession(String session);
    List<VideoSession> findAllByVideoKYCIdOrderByIdDesc(Long videoKYCId);
}
