package ai.obvs.services;

import ai.obvs.Enums.UserType;
import ai.obvs.model.Location;
import ai.obvs.model.VideoParticipant;
import ai.obvs.model.VideoSession;

import java.util.List;

public interface VideoParticipantService {
    void join(Long userId, VideoSession videoSession, UserType userType);
    List<VideoParticipant> getAllVideoParticipants(Long videoSessionId);
    List<VideoParticipant> getAllActiveVideoParticipants(Long videoSessionId);
    List<VideoSession> getAllVideoSessions(Long userId);
    void leave(Long userId, VideoSession videoSession);

    void leaveAll(VideoSession videoSession);

    void updateLocation(Long videoSessionId, Long userId, Location location);
}
