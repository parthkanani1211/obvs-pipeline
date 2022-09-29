package ai.obvs.services.impl;

import ai.obvs.Enums.UserType;
import ai.obvs.model.Location;
import ai.obvs.model.VideoParticipant;
import ai.obvs.model.VideoSession;
import ai.obvs.repository.LocationRepository;
import ai.obvs.repository.VideoParticipantRepository;
import ai.obvs.services.VideoParticipantService;
import ai.obvs.services.VideoSessionService;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
public class VideoParticipantServiceImpl implements VideoParticipantService {

    private VideoParticipantRepository videoParticipantRepository;
    private LocationRepository locationRepository;

    public VideoParticipantServiceImpl(VideoParticipantRepository videoParticipantRepository, LocationRepository locationRepository) {
        this.videoParticipantRepository = videoParticipantRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public void join(Long userId, VideoSession videoSession, UserType userType) {
        VideoParticipant videoParticipant = new VideoParticipant();
        videoParticipant.setVideoSession(videoSession);
        videoParticipant.setUserId(userId);
        videoParticipant.setUserType(userType);
        videoParticipant.setJoinTime(ZonedDateTime.now());
        videoParticipantRepository.save(videoParticipant);
    }

    @Override
    public List<VideoParticipant> getAllVideoParticipants(Long videoSessionId) {
        return videoParticipantRepository.findAllByVideoSessionId(videoSessionId);
    }

    @Override
    public List<VideoParticipant> getAllActiveVideoParticipants(Long videoSessionId) {
        List<VideoParticipant> allByVideoSessionId = videoParticipantRepository.findAllByVideoSessionId(videoSessionId);
        return allByVideoSessionId.stream().filter(videoParticipant -> videoParticipant.getEndTime() == null).collect(Collectors.toList());
    }

    public List<VideoSession> getAllVideoSessions(Long userId) {
        List<VideoParticipant> videoParticipants = videoParticipantRepository.findAllByUserId(userId);
        List<VideoSession> videoSessions = videoParticipants.stream().map(p -> p.getVideoSession()).collect(Collectors.toList());
        return videoSessions;
    }

    @Override
    public void leave(Long userId, VideoSession videoSession) {
        Optional<VideoParticipant> optionalVideoParticipant = videoParticipantRepository.findByUserIdAndVideoSessionId(userId, videoSession.getId());
        if (optionalVideoParticipant.isPresent()) {
            VideoParticipant videoParticipant = optionalVideoParticipant.get();
            videoParticipant.setEndTime(ZonedDateTime.now());
            videoParticipantRepository.save(videoParticipant);
        }
    }

    @Override
    public void leaveAll(VideoSession videoSession) {
        List<VideoParticipant> allVideoParticipants = videoParticipantRepository.findAllByVideoSessionId(videoSession.getId());
        allVideoParticipants.forEach(videoParticipant -> {
            videoParticipant.setEndTime(ZonedDateTime.now());
            videoParticipantRepository.save(videoParticipant);
        });
    }

    @Override
    public void updateLocation(Long videoSessionId, Long userId, Location location) {
        List<VideoParticipant> allVideoParticipants = getAllVideoParticipants(videoSessionId);
        Optional<VideoParticipant> videoParticipantOptional = allVideoParticipants.stream().filter(x -> x.getUserId().equals(userId)).findFirst();
        if (videoParticipantOptional.isPresent()) {
            Location savedLocation = locationRepository.save(location);

            VideoParticipant videoParticipant = videoParticipantOptional.get();
            videoParticipant.setLocation(savedLocation);
            videoParticipantRepository.save(videoParticipant);
        }
    }
}
