package ai.obvs.services;

import ai.obvs.Enums.UserType;
import ai.obvs.model.VideoSession;

import java.util.List;

public interface VideoSessionService {
    VideoSession initSession(String sessionName);

    void saveVideoSession(VideoSession videoSession);

    VideoSession startSession(String sessionName);

    VideoSession startSession(VideoSession videoSession);

    void endSession(VideoSession videoSession);

    VideoSession GetSession(String sessionName);

    List<VideoSession> findAllByVideoKYCId(Long videoKYCId);

    void joinSession(String sessionName, Long userId, UserType userType);

    void leaveSession(String sessionName, Long userId);

    void endSession(String sessionName);

    int getVideoParticipants(String sessionName);
}
