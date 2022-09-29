package ai.obvs.services.impl;

import ai.obvs.Enums.UserType;
import ai.obvs.Enums.VideoSessionStatus;
import ai.obvs.model.*;
import ai.obvs.repository.VideoSessionRepository;
import ai.obvs.services.VideoParticipantService;
import ai.obvs.services.VideoSessionService;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.*;

@Transactional
public class VideoSessionServiceImpl implements VideoSessionService {

    private VideoSessionRepository videoSessionRepository;
    private VideoParticipantService videoParticipantService;
    Map<Long, List<VideoParticipant>> allVideoParticipants = new HashMap<>();

    public VideoSessionServiceImpl(VideoSessionRepository videoSessionRepository, VideoParticipantService videoParticipantService) {
        this.videoSessionRepository = videoSessionRepository;
        this.videoParticipantService = videoParticipantService;
    }

    @Override
    public VideoSession initSession(String sessionName) {
        VideoSession videoSession = new VideoSession();
        videoSession.setSession(sessionName);
        videoSession.setStatus(VideoSessionStatus.INITIATED);
        return videoSession;
    }

    @Override
    public void saveVideoSession(VideoSession videoSession) {
        videoSessionRepository.save(videoSession);
    }

    @Override
    public VideoSession startSession(String sessionName) {
        VideoSession videoSession = getVideoSessionByName(sessionName);
        return startSession(videoSession);
    }

    @Override
    public VideoSession startSession(VideoSession videoSession) {
        videoSession.setStartTime(ZonedDateTime.now());
        videoSession.setStatus(VideoSessionStatus.STARTED);
        return videoSessionRepository.save(videoSession);
    }

    private VideoSession getVideoSessionByName(String sessionName) {
        return videoSessionRepository.findBySession(sessionName);
    }

    @Override
    public void endSession(VideoSession videoSession) {
        videoSession.setStatus(VideoSessionStatus.STOPPED);
        videoSession.setEndTime(ZonedDateTime.now());
        saveVideoSession(videoSession);
    }

    @Override
    public VideoSession GetSession(String sessionName) {
        return videoSessionRepository.findBySession(sessionName);
    }

    @Override
    public List<VideoSession> findAllByVideoKYCId(Long videoKYCId) {
        return videoSessionRepository.findAllByVideoKYCIdOrderByIdDesc(videoKYCId);
    }

    public VideoSession GetSession(Long id) {
        Optional<VideoSession> videoSessionOptional = videoSessionRepository.findById(id);
        if (videoSessionOptional.isPresent())
            return videoSessionOptional.get();

        throw new NoSuchElementException("VideoSession is no longer active");
    }

    @Override
    public void joinSession(String sessionName, Long userId, UserType userType) {
        VideoSession videoSession = GetSession(sessionName);
        Long videoSessionId = videoSession.getId();
        videoParticipantService.join(userId, videoSession, userType);
        List<VideoParticipant> videoParticipants = videoParticipantService.getAllActiveVideoParticipants(videoSessionId);
        allVideoParticipants.put(videoSessionId,videoParticipants);
        if (videoSession.getStatus() == VideoSessionStatus.INITIATED) {
            startSession(videoSession);
        }
    }

    @Override
    public void leaveSession(String sessionName, Long userId) {
        VideoSession videoSession = GetSession(sessionName);
        Long videoSessionId = videoSession.getId();
        videoParticipantService.leave(userId, videoSession);
        List<VideoParticipant> videoParticipants = videoParticipantService.getAllActiveVideoParticipants(videoSessionId);
        allVideoParticipants.put(videoSessionId,videoParticipants);
        if(videoParticipants.size() <= 0 && videoSession.getStatus() == VideoSessionStatus.STARTED) {
            endSession(videoSession);
        }
    }

    @Override
    public void endSession(String sessionName) {
        VideoSession videoSession = GetSession(sessionName);
        videoParticipantService.leaveAll(videoSession);
        endSession(videoSession);
    }

    @Override
    public int getVideoParticipants(String sessionName) {
        VideoSession videoSession = GetSession(sessionName);
        Long videoSessionId = videoSession.getId();
        if(!allVideoParticipants.isEmpty() && allVideoParticipants.containsKey(videoSessionId)){
            return allVideoParticipants.get(videoSessionId).size();
        }
        return 0;
    }
}
