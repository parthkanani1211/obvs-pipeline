package ai.obvs.services.impl;

import ai.obvs.Enums.*;
import ai.obvs.Enums.UserType;
import ai.obvs.Enums.VideoKYCRequestStatus;
import ai.obvs.dto.*;
import ai.obvs.exceptions.CustomerProfileNotFoundException;
import ai.obvs.mapper.*;
import ai.obvs.model.*;
import ai.obvs.model.WorkflowStep;
import ai.obvs.repository.AuditRepository;
import ai.obvs.repository.VideoKYCRepository;
import ai.obvs.services.*;
import ai.obvs.services.CKYC.CKYCProfileVideoKYCService;
import ai.obvs.services.notification.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Transactional
public class VideoKYCRequestsServiceImpl implements VideoKYCRequestsService {

    private VideoSessionService videoSessionService;
    private VideoParticipantService videoParticipantService;
    private KYCService kycService;
    private VideoKYCRepository videoKYCRepository;
    private CustomerService customerService;
    private AgentService agentService;
    private AuditRepository auditRepository;
    private GeoLocationService geoLocationService;
    private UserService userService;
    private OrgService orgService;
    private ObjectMapper objectMapper;
    @Autowired
    private NotificationService notificationService;

    public VideoKYCRequestsServiceImpl(VideoSessionService videoSessionService, VideoParticipantService videoParticipantService, KYCService kycService,
                                       VideoKYCRepository videoKYCRepository, UserService userService, CustomerService customerService, AgentService agentService,
                                       AuditRepository auditRepository, GeoLocationService geoLocationService, OrgService orgService) {
        this.videoSessionService = videoSessionService;
        this.videoParticipantService = videoParticipantService;
        this.kycService = kycService;
        this.videoKYCRepository = videoKYCRepository;
        this.userService = userService;
        this.customerService = customerService;
        this.agentService = agentService;
        this.auditRepository = auditRepository;
        this.geoLocationService = geoLocationService;
        this.orgService = orgService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Optional<VideoKYC> getVideoKYC(Long videoKycId) {
        return videoKYCRepository.findById(videoKycId);
    }

    @Override
    public VideoKYCRequestBaseDto create(Long orgId, Long createdBy, VKYCRequestDto vkycRequestDto) {

        Long customerId = vkycRequestDto.getCustomerId() != null ? vkycRequestDto.getCustomerId() : createdBy;

        Optional<Customer> optionalCustomer = customerService.getById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            VideoKYC videoKYC = new VideoKYC();
            videoKYC.setMobileNumber(customer.getMobileNumber());
            videoKYC.setVideoKYCRequestStatus(VideoKYCRequestStatus.INITIATED);
            videoKYC.setCreatedBy(createdBy);
            videoKYC.setCreatedOn(ZonedDateTime.now());
            videoKYC.setUpdatedBy(createdBy);
            videoKYC.setUpdatedOn(ZonedDateTime.now());
            videoKYC.setCustomer(customer);
            Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
            if (organizationById.isPresent()) {
                videoKYC.setOrganization(organizationById.get());
            }

            VideoKYC savedVideoKYC = saveVideoKYC(videoKYC, createdBy);

            String sessionName = customer.getMobileNumber() + "_" + UUID.randomUUID();
            VideoSession videoSession = videoSessionService.initSession(sessionName);
            videoSession.setVideoKYC(videoKYC);
            videoSessionService.saveVideoSession(videoSession);

            List<VideoSession> videoSessionList = new ArrayList<>();
            videoSessionList.add(videoSession);
            videoKYC.setVideoSessions(videoSessionList);

            return getVideoKYCRequestBaseDto(savedVideoKYC, sessionName);
        }

        throw new IllegalArgumentException("No customer exists with this mobile number");
    }

    @Override
    public VideoKYCRequestBaseDto restart(Long videoKycId, VideoKYCRequestDto VideoKYCRequestDto) {

        Optional<Customer> optionalCustomer = customerService.getById(VideoKYCRequestDto.getUserId());
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(videoKycId);
            if (videoKYCOptional.isPresent()) {
                VideoKYC videoKYC = videoKYCOptional.get();

                Optional<VideoSession> videoSessionOptional = findActiveVideoSession(videoKYC);
                if (videoSessionOptional.isPresent()) {
                    VideoSession videoSession = videoSessionOptional.get();
                    return getVideoKYCRequestBaseDto(videoKYC, videoSession.getSession());
                } else {
                    String sessionName = assignNewSession(videoKYC);
                    VideoKYC savedVideoKYC = videoKYCRepository.save(videoKYC);
                    return getVideoKYCRequestBaseDto(savedVideoKYC, sessionName);
                }
            } else {
                throw new IllegalArgumentException("No requests are in progress");
            }
        }
        throw new IllegalArgumentException("No customer exists with this mobile number");
    }

    @Override
    public VideoKYCRequestBaseDto restart(Long videoKycId, Long userId) {

        Optional<Customer> optionalCustomer = customerService.getById(userId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(videoKycId);
            if (videoKYCOptional.isPresent()) {
                VideoKYC videoKYC = videoKYCOptional.get();

                Optional<VideoSession> videoSessionOptional = findActiveVideoSession(videoKYC);
                if (videoSessionOptional.isPresent()) {
                    VideoSession videoSession = videoSessionOptional.get();
                    return getVideoKYCRequestBaseDto(videoKYC, videoSession.getSession());
                } else {
                    String sessionName = assignNewSession(videoKYC);
                    VideoKYC savedVideoKYC = videoKYCRepository.save(videoKYC);
                    return getVideoKYCRequestBaseDto(savedVideoKYC, sessionName);
                }
            } else {
                throw new IllegalArgumentException("No requests are in progress");
            }
        }
        throw new IllegalArgumentException("No customer exists with this mobile number");
    }

    private String assignNewSession(VideoKYC videoKYC) {
        String sessionName = videoKYC.getMobileNumber() + "_" + UUID.randomUUID();

        VideoSession videoSessionToSave = videoSessionService.initSession(sessionName);
        videoSessionToSave.setVideoKYC(videoKYC);
        List<VideoSession> videoSessions = videoKYC.getVideoSessions();
        videoSessions.add(videoSessionToSave);
        videoKYC.setVideoSessions(videoSessions);
        return sessionName;
    }

    @Override
    public Optional<VideoSession> findActiveSession(Long videoKycId) {
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(videoKycId);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();

            List<VideoSession> allByVideoKYCId = getAllByVideoKYCId(videoKYC);
            List<VideoSession> activeVideoSessions = allByVideoKYCId.stream()
                    .filter(x -> x.getStatus().equals(VideoSessionStatus.STARTED))
                    .collect(Collectors.toList());

            if (activeVideoSessions.size() > 0) {
                VideoSession videoSession = activeVideoSessions.get(0);
                return Optional.of(videoSession);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<VideoSession> findLastCompletedSession(Long videoKycId) {
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(videoKycId);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();

            List<VideoSession> activeVideoSessions = getAllByVideoKYCId(videoKYC);
//            List<VideoSession> activeVideoSessions = allByVideoKYCId.stream()
//                    .filter(x -> x.getStatus().equals(VideoSessionStatus.STOPPED))
//                    .collect(Collectors.toList());

            if (activeVideoSessions.size() > 0) {
                VideoSession videoSession = activeVideoSessions.get(0);
                return Optional.of(videoSession);
            }
        }
        return Optional.empty();
    }

    @Override
    public void cancelVKYCRequest(Long videoKycId, Long userId, VideoKYCRequestStatusDto videoKYCRequestStatusDto, VideoKYCRequestStatus videoKYCRequestStatus) {
        Optional<User> optionalCustomer = userService.getUserById(userId);
        if (optionalCustomer.isPresent()) {
            Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(videoKycId);
            if (videoKYCOptional.isPresent()) {
                VideoKYC videoKYC = videoKYCOptional.get();

                Optional<VideoSession> videoSessionOptional = findActiveVideoSession(videoKYC);
                if (videoSessionOptional.isPresent()) {
                    VideoSession videoSession = videoSessionOptional.get();
                    videoSessionService.endSession(videoSession);
                }

                updateStatus(videoKYC, videoKYCRequestStatus, userId);
            }
        }
    }

    private VideoKYCRequestBaseDto getVideoKYCRequestBaseDto(VideoKYC savedVideoKYC, String sessionName) {
        VideoKYCRequestBaseDto videoKYCRequestBaseDto = VideoKYCMapper.MAPPER.ToVideoKYCBaseDto(savedVideoKYC);
        videoKYCRequestBaseDto.setSessionName(sessionName);
        return videoKYCRequestBaseDto;
    }

    private Optional<VideoSession> findActiveVideoSession(VideoKYC videoKYC) {
        List<VideoSession> allByVideoKYCId = getAllByVideoKYCId(videoKYC);
        List<VideoSession> activeVideoSessions = allByVideoKYCId.stream().filter(x -> x.getStatus().equals(VideoSessionStatus.STARTED)
                || x.getStatus().equals(VideoSessionStatus.INITIATED)).collect(Collectors.toList());

        if (activeVideoSessions.size() > 0) {
            VideoSession videoSession = activeVideoSessions.get(0);
            if (activeVideoSessions.size() > 1) {
                allByVideoKYCId.forEach(session -> {
                    if (session.getId() != videoSession.getId()) {
                        session.setStatus(VideoSessionStatus.STOPPED);
                    }
                });
                videoKYC.setVideoSessions(allByVideoKYCId);
                videoKYC.setUpdatedOn(ZonedDateTime.now());
//                videoKYCRepository.save(videoKYC);
            }

            return Optional.of(videoSession);
        }
        return Optional.empty();
    }

    private List<VideoSession> getAllByVideoKYCId(VideoKYC videoKYC) {
        return videoSessionService.findAllByVideoKYCId(videoKYC.getId());
    }

    @Override
    public VideoSessionDto update(Long id, Long servedBy) {

        Optional<Agent> optionalAgent = agentService.getById(servedBy);

        if (optionalAgent.isPresent()) {
            Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(id);
            if (videoKYCOptional.isPresent()) {
                VideoKYC videoKYC = videoKYCOptional.get();
                VideoSession existingVideoSession = null; //videoKYC.getVideoSession();
                if (existingVideoSession == null) {
                    VideoSession videoSession = videoSessionService.startSession(videoKYC.getCustomer().getMobileNumber().toString());
//                    videoKYC.setVideoSession(videoSession);
//                    videoParticipantService.join(servedBy, videoSession);
//                    videoParticipantService.join(videoKYC.getCustomer().getId(), videoSession);
//                    updateStatus(videoKYC, VideoKYCRequestStatus.IN_PROGRESS);
                    return VideoSessionMapper.MAPPER.ToVideoSessionDto(videoSession);
                } else
                    throw new IllegalArgumentException("VideoSession is already in progress.");
            } else {
                throw new IllegalArgumentException("Video KYC request no longer exists");
            }
        } else {
            throw new RuntimeException("User not having sufficient permission");
        }
    }

    @Override
    public void joinSession(Long videoKycId, Long userId) {
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(videoKycId);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();
            Optional<VideoSession> existingVideoSessionOptional = findActiveVideoSession(videoKYC);
            if (existingVideoSessionOptional.isPresent()) {
                VideoSession existingVideoSession = existingVideoSessionOptional.get();
                joinSession(videoKycId, existingVideoSession.getSession(), userId);
            } else {
                throw new IllegalArgumentException("VideoSession is no longer active.");
            }
        } else {
            throw new IllegalArgumentException("Video KYC request no longer exists");
        }
    }

    @Override
    public void leaveSession(Long videoKycId, Long userId) {
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(videoKycId);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();
            Optional<VideoSession> existingVideoSessionOptional = findActiveVideoSession(videoKYC);
            if (existingVideoSessionOptional.isPresent()) {
                VideoSession existingVideoSession = existingVideoSessionOptional.get();
                leaveSession(videoKycId, existingVideoSession.getSession(), userId);
            } else {
                throw new IllegalArgumentException("VideoSession is no longer active.");
            }
        } else {
            throw new IllegalArgumentException("Video KYC request no longer exists");
        }
    }

    @Override
    public void endSession(Long videoKycId, Long userId) {
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(videoKycId);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();
            Optional<VideoSession> existingVideoSessionOptional = findActiveVideoSession(videoKYC);
            if (existingVideoSessionOptional.isPresent()) {
                VideoSession existingVideoSession = existingVideoSessionOptional.get();
                endSession(videoKycId, existingVideoSession.getSession());
                if (videoKYC.getVideoKYCRequestStatus().equals(VideoKYCRequestStatus.IN_PROGRESS)
                        || videoKYC.getVideoKYCRequestStatus().equals(VideoKYCRequestStatus.PENDING)) {
                    String newSessionName = assignNewSession(videoKYC);
                    updateStatus(videoKYC, VideoKYCRequestStatus.PENDING, userId);
                } else {
                    throw new IllegalArgumentException("VideoSession is no longer active.");
                }
            } else {
                throw new IllegalArgumentException("VideoSession is no longer active.");
            }
        } else {
            throw new IllegalArgumentException("Video KYC request no longer exists");
        }
    }

    @Override
    public void joinSession(Long videoKycId, String sessionName, Long userId) {
        boolean isUserJoined = false;
        Optional<Agent> agentOptional = agentService.getById(userId);
        if (agentOptional.isPresent()) {
            videoSessionService.joinSession(sessionName, userId, UserType.AGENT);
            isUserJoined = true;
        } else {
            Optional<Customer> customerOptional = customerService.getById(userId);
            if (customerOptional.isPresent()) {
                videoSessionService.joinSession(sessionName, userId, UserType.CUSTOMER);
                isUserJoined = true;
            }
        }
        if (isUserJoined) {
            Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(videoKycId);
            if (videoKYCOptional.isPresent()) {
                updateStatus(videoKYCOptional.get(), VideoKYCRequestStatus.IN_PROGRESS, userId);
            }
        }
    }

    @Override
    public void leaveSession(Long videoKycId, String sessionName, Long userId) {
        videoSessionService.leaveSession(sessionName, userId);
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(videoKycId);
        if (videoKYCOptional.isPresent()) {
            int videoParticipantsCount = videoSessionService.getVideoParticipants(sessionName);
            if (videoParticipantsCount <= 0) {
                VideoKYC videoKYC = videoKYCOptional.get();
                if (videoKYC.getVideoKYCRequestStatus() == VideoKYCRequestStatus.IN_PROGRESS) {
                    String newSessionName = assignNewSession(videoKYC);
                    updateStatus(videoKYC, VideoKYCRequestStatus.PENDING, userId);
                }
            }
        }
    }

    @Override
    public void endSession(Long videoKycId, String sessionName) {
        videoSessionService.endSession(sessionName);
    }

    @Override
    public String updateUserLocation(Long videoKycId, LocationRequestDto locationRequestDto) {
        String locationString = "";
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(videoKycId);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();
            Optional<VideoSession> existingVideoSessionOptional = findActiveVideoSession(videoKYC);
            if (existingVideoSessionOptional.isPresent()) {
                VideoSession existingVideoSession = existingVideoSessionOptional.get();
                LocationResponseDto locationResponseDto = geoLocationService.getLocation(locationRequestDto);
                locationString = locationResponseDto.getLocation();
                Location location = new Location();
                location.setLatitude(Float.valueOf(locationRequestDto.getLatitude()));
                location.setLongitude(Float.valueOf(locationRequestDto.getLongitude()));
                location.setLocation(locationString);
                videoParticipantService.updateLocation(existingVideoSession.getId(), videoKYC.getCustomer().getId(), location);
            } else
                throw new IllegalArgumentException("There is no video session in progress.");
        } else {
            throw new IllegalArgumentException("Video KYC request no longer exists");
        }
        return locationString;
    }

    @Override
    public Optional<AuditResponseDto> sendRequestForAudit(Long id, Long userId, VideoKYCRequestStatusDto videoKYCRequestStatusDto) {
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(id);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();
            videoKYC.setComment(videoKYCRequestStatusDto.getComment());
            Optional<Agent> optionalAgent = agentService.getById(userId);
            if (optionalAgent.isPresent())
                videoKYC.setAgent(optionalAgent.get());
            updateStatus(videoKYC, VideoKYCRequestStatus.COMPLETED, userId);
            Optional<VideoSession> videoSessionOptional = findActiveVideoSession(videoKYC);
            if (videoSessionOptional.isPresent()) {
                VideoSession videoSession = videoSessionOptional.get();
                List<VideoParticipant> allVideoParticipants = videoParticipantService.getAllVideoParticipants(videoSession.getId());
                List<VideoParticipant> agents = allVideoParticipants.stream().filter(videoParticipant -> agentService.getById(videoParticipant.getUserId()).isPresent()).collect(Collectors.toList());
                if (agents.size() > 0) {
                    Long agentId = agents.get(0).getUserId();
                    AuditRequestDto auditRequestDto = new AuditRequestDto();
                    auditRequestDto.setCustomerId(videoKYC.getCustomer().getId());
                    auditRequestDto.setAgentId(agentId);
                    auditRequestDto.setVideoKYCId(videoKYC.getId());
                    Optional<AuditResponseDto> auditResponseDto = saveAudit(auditRequestDto);
                    notificationService.notifyVKYCCompleted(videoKYC.getCustomer().getMobileNumber());
                    return auditResponseDto;
                }
            }
            return Optional.empty();
        }
        else {
            throw new IllegalArgumentException("Video KYC request no longer exists");
        }
    }

    @Override
    public void rejectVKYCRequest(Long id, Long userId, VideoKYCRequestStatusDto videoKYCRequestStatusDto) {
        VideoKYC videoKYC = getVideoKYC(id, videoKYCRequestStatusDto);
        setAuditor(userId, videoKYC);
        updateStatus(videoKYC, VideoKYCRequestStatus.REJECTED, userId);
        notificationService.notifyVKYCRejected(videoKYC.getCustomer().getMobileNumber());
    }

    @Override
    public void approveVKYCRequest(Long id, Long userId, VideoKYCRequestStatusDto videoKYCRequestStatusDto) {
        VideoKYC videoKYC = getVideoKYC(id, videoKYCRequestStatusDto);
        setAuditor(userId, videoKYC);
        updateStatus(videoKYC, VideoKYCRequestStatus.APPROVED, userId);
        notificationService.notifyVKYCApproved(videoKYC.getCustomer().getMobileNumber());
    }

    private void setAuditor(Long userId, VideoKYC videoKYC) {
        Optional<Agent> optionalAgent = agentService.getById(userId);
        if (optionalAgent.isPresent()) {
            Agent auditor = optionalAgent.get();
//            boolean isUserAllowed = auditor.getRoles().stream().anyMatch(r -> r.getName().equals(Roles.AUDITOR.getValue()) ||
//                    r.getName().equals(Roles.ADMIN.getValue()) || r.getName().equals(Roles.SUPERADMIN.getValue()) || r.getName().equals(Roles.Agent.getValue()));
//            if (isUserAllowed) {
            boolean isUserAllowed = auditor.getRoles().stream().anyMatch(r -> r.getName().equals(Roles.CHECKER.getValue()) ||
                r.getName().equals(Roles.ADMIN.getValue()) || r.getName().equals(Roles.SUPERADMIN.getValue()) || r.getName().equals(Roles.MAKER.getValue()) );
            if (isUserAllowed) {
                videoKYC.setAuditor(auditor);
            }
//            else
//                throw new InsufficientAuthenticationException("Insufficient permission to the user");
        } else
            throw new IllegalArgumentException("User is not registered with the system");
    }

    @Override
    public void submitVKYCRequest(Long id, Long userId, VideoKYCRequestStatusDto videoKYCRequestStatusDto) {
        VideoKYC videoKYC = getVideoKYC(id, videoKYCRequestStatusDto);
        updateStatus(videoKYC, VideoKYCRequestStatus.PENDING_APPROVAL, userId);
    }

    private VideoKYC getVideoKYC(Long id, VideoKYCRequestStatusDto videoKYCRequestStatusDto) {
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(id);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();
            videoKYC.setComment(videoKYCRequestStatusDto.getComment());
            return videoKYC;
        } else {
            throw new IllegalArgumentException("Video KYC request no longer exists");
        }
    }

    private void updateStatus(VideoKYC videoKYC, VideoKYCRequestStatus videoKYCRequestStatus, Long userId) {
        videoKYC.setVideoKYCRequestStatus(videoKYCRequestStatus);
        saveVideoKYC(videoKYC, userId);
    }

    private VideoKYC saveVideoKYC(VideoKYC videoKYC, Long userId) {
        videoKYC.setUpdatedOn(ZonedDateTime.now());
        videoKYC.setUpdatedBy(userId);
        return videoKYCRepository.save(videoKYC);
    }

    @Override
    public List<VKYCResponseDto> getAllRequests(Long orgId, User user) {
        List<VideoKYC> videoKYCList = new ArrayList<>();
        Optional<User> userById = userService.getUserById(user.getId());

        if (userById.isPresent()) {
            User user1 = userById.get();
            Set<Role> roles = user1.getRoles();

            Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
            if (organizationById.isPresent()) {
                boolean isCustomer = roles.stream().anyMatch(r -> r.getName().equals(Roles.CUSTOMER.getValue()));
                if (isCustomer)
                    videoKYCList = videoKYCRepository.findAllByOrganizationAndCustomerIdOrderByIdDesc(organizationById.get(), user.getId());
                else
                    videoKYCList = videoKYCRepository.findAllByOrganizationOrderByIdDesc(organizationById.get());
            }
            List<VKYCResponseDto> vkycResponseDtoList = VKYCRequestMapper.MAPPER.TOVKYCResponseDTOList(videoKYCList);
            getVideoKYCResponse(videoKYCList, vkycResponseDtoList, false);
            return vkycResponseDtoList;
        }
        return new ArrayList<>();
    }

    @Override
    public List<VKYCResponseDto> getAllRequestsByStatus(Long orgId, User user, String videoKYCRequestStatusValue) {
        VideoKYCRequestStatus videoKYCRequestStatus = VideoKYCRequestStatus.valueOf(videoKYCRequestStatusValue.toUpperCase());

        List<VideoKYC> videoKYCList = new ArrayList<>();
        Optional<User> userById = userService.getUserById(user.getId());

        if (userById.isPresent()) {
            User user1 = userById.get();
            Set<Role> roles = user1.getRoles();
            Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
            boolean isValidUser = false;

            if (organizationById.isPresent()) {
                switch (videoKYCRequestStatus) {
                    case PENDING_APPROVAL:
                    case REJECTED:
                    case APPROVED:
//                        boolean isAuditor = roles.stream().anyMatch(r -> r.getName().equals(Roles.AUDITOR.getValue()));
//                        isValidUser = isAuditor;
                        boolean isChecker = roles.stream().anyMatch(r -> r.getName().equals(Roles.CHECKER.getValue()));
                        isValidUser = isChecker;
                        break;
                    case INITIATED:
                    case IN_PROGRESS:
                    case PENDING:
                    case COMPLETED:
//                        boolean isAgent = roles.stream().anyMatch(r -> r.getName().equals(Roles.AGENT.getValue()));
                        boolean isAgent = roles.stream().anyMatch(r -> r.getName().equals(Roles.MAKER.getValue()));
                        isValidUser = isAgent;
                        break;
                }
                if (isValidUser) {
                    videoKYCList = videoKYCRepository.findAllByOrganizationAndVideoKYCRequestStatusOrderByIdDesc(organizationById.get(), videoKYCRequestStatus);
                }
//                        if (isAgent) {
//                            List<VideoSession> allVideoSessions = videoParticipantService.getAllVideoSessions(user1.getId());
//                            List<VideoKYC> videoKYCSet = allVideoSessions.stream().map(videoSession -> videoSession.getVideoKYC()).collect(Collectors.toList());
//
//                            videoKYCList = videoKYCRepository.findAllByOrganizationAndCustomerIdOrderByIdDesc(organizationById.get(), user1.getId());
//                        }
//                        break;
            }

            List<VKYCResponseDto> vkycResponseDtoList = VKYCRequestMapper.MAPPER.TOVKYCResponseDTOList(videoKYCList);
            getVideoKYCResponse(videoKYCList, vkycResponseDtoList, false);
            return vkycResponseDtoList;
        }

        return new ArrayList<>();
    }

    private void getVideoKYCResponse(List<VideoKYC> activeVideoKYCRequests, List<VKYCResponseDto> vkycResponseDtoList, boolean isFromAgent) {
        vkycResponseDtoList.forEach(vkycResponseDto -> {
            Optional<VideoKYC> videoKYCOptional = activeVideoKYCRequests.stream().filter(x -> x.getId().equals(vkycResponseDto.getId())).findFirst();
            if (videoKYCOptional.isPresent()) {
                VideoKYC videoKYC = videoKYCOptional.get();
                if (videoKYC.getCustomer() != null) {
                    vkycResponseDto.setCustomerDto(CustomerMapper.MAPPER.ToCustomerDto(videoKYC.getCustomer()));
                }
                Long createdBy = videoKYC.getCreatedBy();
                UserDto userDto = userService.findById(createdBy);
                if (userDto != null) {
                    String userName = "";
                    if (userDto.getFirstName() != null)
                        userName = userDto.getFirstName();
                    if (userDto.getLastName() != null)
                        userName = userName + " " + userDto.getLastName();
                    vkycResponseDto.setInitiatedBy(userName);
                }

                Agent agent = videoKYC.getAgent();
                if (agent != null) {
                    AgentDto agentDto = AgentMapper.MAPPER.ToAgentDto(agent);
                    vkycResponseDto.setAgentDto(agentDto);
                } else {
                    Optional<VideoSession> activeVideoSession = findActiveVideoSession(videoKYC);
                    if (activeVideoSession.isPresent()) {
                        VideoSession videoSession = activeVideoSession.get();
                        vkycResponseDto.setSessionName(activeVideoSession.get().getSession());
                        if (!isFromAgent) {
                            setAgent(vkycResponseDto, videoSession);
                        }
                    } else if (!isFromAgent) {
                        List<VideoSession> videoSessions = getAllByVideoKYCId(videoKYC);
                        if (videoSessions.size() > 0) {
                            VideoSession videoSession = videoSessions.get(0);
                            setAgent(vkycResponseDto, videoSession);
                        }
                    }
                }

                Agent auditor = videoKYC.getAuditor();
                if (auditor != null) {
                    AgentDto auditorDto = AgentMapper.MAPPER.ToAgentDto(auditor);
                    vkycResponseDto.setAuditorDto(auditorDto);
                }
            }
        });
    }

    private void setAgent(VKYCResponseDto vkycResponseDto, VideoSession videoSession) {
        List<VideoParticipant> allVideoParticipants = videoParticipantService.getAllVideoParticipants(videoSession.getId());
        List<Agent> agents = allVideoParticipants.stream()
                .filter(videoParticipant -> videoParticipant.getUserType().equals(UserType.AGENT))
                .map(videoParticipant -> agentService.getById(videoParticipant.getUserId()).get())
                .collect(Collectors.toList());
//                        VideoSessionAttendeesDto videoSessionAttendeesDto = VideoSessionMapper.MAPPER.ToVideoSessionAttendeeDto(videoSession);
        if (agents.size() > 0) {
            AgentDto agentDto = AgentMapper.MAPPER.ToAgentDto(agents.get(0));
            vkycResponseDto.setAgentDto(agentDto);
//                            videoSessionAttendeesDto.setAgentDto(agentDto);
//                            videoSessionAttendeesDto.setSession(videoSession.getSession());
        }
    }

    @Override
    public List<VKYCResponseDto> getRequestsByUserId(Long userId) {
        List<VideoSession> videoSessions = videoParticipantService.getAllVideoSessions(userId);
        Set<Long> videoSessionIds = videoSessions.stream().map(v -> v.getId()).collect(Collectors.toSet());
        List<VideoKYC> videoKYCList = videoKYCRepository.findAllById(videoSessionIds);
        List<VKYCResponseDto> vkycResponseDtoList = VKYCRequestMapper.MAPPER.TOVKYCResponseDTOList(videoKYCList);
        getVideoKYCResponse(videoKYCList, vkycResponseDtoList, false);
        return vkycResponseDtoList;
    }

    @Override
    public List<VKYCResponseDto> getRequestsByCustomerId(Long orgId, Long userId) {
        List<VideoKYC> videoKYCList = new ArrayList<>();
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
        if (organizationById.isPresent()) {
            videoKYCList = videoKYCRepository.findAllByOrganizationAndCustomerIdOrderByIdDesc(organizationById.get(), userId);
        }

//        videoKYCList = videoKYCRepository.findAllByCustomerIdOrderByIdDesc(userId);
        List<VKYCResponseDto> vkycResponseDtoList = VKYCRequestMapper.MAPPER.TOVKYCResponseDTOList(videoKYCList);

        getVideoKYCResponse(videoKYCList, vkycResponseDtoList, false);
        return vkycResponseDtoList;
    }

    @Override
    public List<KYCDto> getKYCByVideoSession(String session) {
        VideoSession videoSession = videoSessionService.GetSession(session);
        return kycService.getKYCDtoByVideoSessionId(videoSession.getId());
    }

    @Override
    public List<KYCDto> getKYCDataList(Long id) {
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(id);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();

            List<VideoSession> videoSessions = videoKYC.getVideoSessions();
            List<KYCDto> kycDtoList = new ArrayList<>();
            videoSessions.forEach(videoSession -> {
                List<KYCDto> kycDtoByVideoSessionId = kycService.getKYCDtoByVideoSessionId(videoSession.getId());
                kycDtoList.addAll(kycDtoByVideoSessionId);
            });
            return kycDtoList;
        }
        return new ArrayList<>();
    }

    @Override
    public List<KYC> getKYCList(Long id) {
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(id);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();

            List<VideoSession> videoSessions = videoKYC.getVideoSessions();
            List<KYC> kycList = new ArrayList<>();
            videoSessions.forEach(videoSession -> {
                List<KYC> kycByVideoSessionId = kycService.getKYCByVideoSessionId(videoSession.getId());
                kycList.addAll(kycByVideoSessionId);
            });
            return kycList;
        }
        return new ArrayList<>();
    }

    @Override
    public List<WorkflowStep> getCompletedKYCSteps(Long id) {
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(id);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();

            List<VideoSession> videoSessions = videoKYC.getVideoSessions();
            List<KYC> kycList = new ArrayList<>();
            videoSessions.forEach(videoSession -> {
                List<KYC> kycByVideoSessionId = kycService.getKYCByVideoSessionId(videoSession.getId());
                kycList.addAll(kycByVideoSessionId);
            });
            List<WorkflowStep> workflowSteps = kycList.stream()
                    .filter(k -> k.getVerified()).map(k -> k.getWorkflowStep())
                    .collect(Collectors.toList());
            return workflowSteps;
        }
        return new ArrayList<>();
    }

    @Override
    public KYCAuditDto getKYCAuditData(Long id) {
        KYCAuditDto kycAuditDto = new KYCAuditDto();
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(id);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();
            kycAuditDto.setCustomerDto(CustomerMapper.MAPPER.ToCustomerDto(videoKYC.getCustomer()));

            List<VideoSession> videoSessions = getAllByVideoKYCId(videoKYC);

            List<KYCResourcesDto> kycResourcesDtoList = new ArrayList<>();
            videoSessions.forEach(videoSession -> {
                List<KYCResourcesDto> kycResourcesByVideoSessionId = kycService.getKYCResourcesByVideoSessionId(videoSession.getId());
                kycResourcesDtoList.addAll(kycResourcesByVideoSessionId);
            });

            kycAuditDto.setKycResourcesDtoList(kycResourcesDtoList);

            kycAuditDto.setStatus(videoKYC.getVideoKYCRequestStatus());
            kycAuditDto.setComment(videoKYC.getComment());
            List<VideoParticipant> allVideoParticipants = videoParticipantService.getAllVideoParticipants(videoSessions.get(0).getId());
            List<Agent> agents = allVideoParticipants.stream()
                    .filter(videoParticipant -> agentService.getById(videoParticipant.getUserId()).isPresent())
                    .map(videoParticipant -> agentService.getById(videoParticipant.getUserId()).get())
                    .collect(Collectors.toList());
            if (agents.size() > 0) {
                kycAuditDto.setAgentDto(AgentMapper.MAPPER.ToAgentDto(agents.get(0)));
            }

            Optional<VideoParticipant> videoParticipantOptional = allVideoParticipants.stream().filter(videoParticipant -> videoParticipant.getUserId().equals(videoKYC.getCustomer().getId())).findFirst();
            if (videoParticipantOptional.isPresent()) {
                VideoParticipant videoParticipant = videoParticipantOptional.get();
                Location location = videoParticipant.getLocation();
                if (location != null) {
                    String locationData = location.getLocation();
                    JSONObject object = null;
                    try {
                        if (locationData != null) {
                            object = (JSONObject) new JSONParser().parse(locationData);
                            String address = object.get("display_name").toString();
                            kycAuditDto.setCustomerLocation(address);
                        }
                    } catch (ParseException e) {
                    }
                }
            }
        }
        return kycAuditDto;
    }

    @Override
    public Customer getCustomerDataByVideoKYCRequestId(Long id) {
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(id);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();
            return videoKYC.getCustomer();
        }

        throw new CustomerProfileNotFoundException("Unable to find Video KYC Request for Customer");
    }

    @Override
    public KYCDto getKYCData(Long id, String workflowStep) {
//        KYCStep kycStep = KYCStep.valueOf(workflowStep.toUpperCase());
        Optional<VideoKYC> videoKYCOptional = videoKYCRepository.findById(id);
        if (videoKYCOptional.isPresent()) {
            VideoKYC videoKYC = videoKYCOptional.get();
            List<VideoSession> videoSessions = videoKYC.getVideoSessions();
            AtomicReference<KYCDto> result = new AtomicReference<>();

            videoSessions.forEach(videoSession -> {
                KYCDto kycDto = kycService.getKYC(videoSession.getId(), workflowStep);
                if (kycDto != null) {
                    result.set(kycDto);
                }
            });
            return result.get();
        }
        return null;
    }

    @Override
    public Optional<AuditResponseDto> saveAudit(AuditRequestDto auditRequestDto) {
        try {
            Audit audit = new Audit();
            audit.setAgentId(auditRequestDto.getAgentId());
            audit.setCustomerId(auditRequestDto.getCustomerId());
            audit.setVideoKYCId(auditRequestDto.getVideoKYCId());
            audit.setRequestTime(ZonedDateTime.now());
            Audit savedAudit = auditRepository.save(audit);
            AuditResponseDto auditResponseDto = new AuditResponseDto();
            auditResponseDto.setAuditId(savedAudit.getId());
            auditResponseDto.setVideoKYCId(auditRequestDto.getVideoKYCId());
            return Optional.of(auditResponseDto);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public AuditDataDto getAuditDetail(Long videoKYCId) {
        AuditDataDto auditDataDto = new AuditDataDto();
//
//        Optional<Audit> optionalAudit = auditRepository.findById(auditId);
//        if (optionalAudit.isPresent()) {
//            Audit audit = optionalAudit.get();
//        Long videoKYCId = audit.getVideoKYCId();
        KYCAuditDto kycAuditData = getKYCAuditData(videoKYCId);
        List<KYCResourcesDto> kycDataList = kycAuditData.getKycResourcesDtoList();
        Map<String, KYCVerificationData> kycTypeKYCVerificationDataMap = new HashMap<>();
        AtomicReference<String> customerName = new AtomicReference<>("");
        kycDataList.forEach(kycDto -> {
//            KYCType kycType = kycDto.getKycType();
            KYCVerificationData kycVerificationData = new KYCVerificationData();
            try {
                kycVerificationData = objectMapper.readValue(kycDto.getKYCVerificationData(), KYCVerificationData.class);
//                if (kycType == KYCType.PAN) {
//                    Optional<KYCDataMatchResult> kycDataMatchResult = kycVerificationData.getMatchResults().stream()
//                            .filter(m -> m.getName().equals("Name")).findFirst();
//                    customerName.set(kycDataMatchResult.get().getActualValue());
//                }
            } catch (JsonProcessingException e) {
            }

            List<Image> imageStringList = new ArrayList<>();
            kycDto.getResources().forEach(r -> {
                Image image = new Image();
                image.setImageData(Base64.getEncoder().encodeToString(r.getContent()));
                imageStringList.add(image);
            });
            kycVerificationData.setImageList(imageStringList);

            String stepName = kycDto.getWorkflowStepDto().getName();
            try {
                ai.obvs.Enums.WorkflowStep workflowStep = ai.obvs.Enums.WorkflowStep.valueOf(stepName);
                stepName = workflowStep.getValue();
            } catch (Exception ex) {
            }
            kycTypeKYCVerificationDataMap.put(stepName, kycVerificationData);
        });
//        if (!StringUtils.isEmpty(customerName.get())
//                && StringUtils.isEmpty(kycAuditData.getCustomerDto().getFirstName())) {
//            kycAuditData.getCustomerDto().setFirstName(customerName.get());
//            kycAuditData.getCustomerDto().setLastName("");
//        }

        auditDataDto.setKycTypeKYCVerificationDataMap(kycTypeKYCVerificationDataMap);
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setCustomerDto(kycAuditData.getCustomerDto());
        customerInfo.setLocation(kycAuditData.getCustomerLocation());
        auditDataDto.setCustomerInfo(customerInfo);
        auditDataDto.setAgentDto(kycAuditData.getAgentDto());
        auditDataDto.setStatus(kycAuditData.getStatus());
        auditDataDto.setComment(kycAuditData.getComment());
//        }
        return auditDataDto;
    }

    @Override
    public List<VKYCResponseDto> getRequestsInitiatedByMobileNumber(Long orgId, Long mobileNumber) {
        List<VideoKYC> videoKYCList = getVideoKYCRequestsByMobileNumber(orgId, mobileNumber);
        List<VKYCResponseDto> vkycResponseDtoList = VKYCRequestMapper.MAPPER.TOVKYCResponseDTOList(videoKYCList);
        getVideoKYCResponse(videoKYCList, vkycResponseDtoList, false);
        return vkycResponseDtoList;
    }

    @Override
    public VKYCResponseDto getRequestInitiatedByMobileNumber(Long orgId, Long mobileNumber) {
        List<VKYCResponseDto> vkycResponseDtos = getRequestsInitiatedByMobileNumber(orgId, mobileNumber);
        if (vkycResponseDtos.size() > 1) {
            return vkycResponseDtos.get(0);
        }
        return new VKYCResponseDto();
    }

    @Override
    public VideoKYC getVideoKYCRequestByMobileNumber(Long orgId, Long mobileNumber) {
        List<VideoKYC> videoKYCRequests = getVideoKYCRequestsByMobileNumber(orgId, mobileNumber);
        if (videoKYCRequests.size() > 0)
            return videoKYCRequests.get(0);
        else
            throw new IllegalArgumentException("Video KYC session is not initiated yet.");
    }

    private List<VideoKYC> getVideoKYCRequestsByMobileNumber(Long orgId, Long mobileNumber) {
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
        if (!organizationById.isPresent()) {
            throw new IllegalArgumentException("Invalid OrgId.");
        }
        Optional<User> userByMobileNumber = userService.getUserByMobileNumber(mobileNumber);
        if (!userByMobileNumber.isPresent()) {
            throw new IllegalArgumentException("User is not registered with this mobile number");
        }
        User user = userByMobileNumber.get();
        boolean isCustomer = user.getRoles().stream().anyMatch(r -> r.getName().equals(Roles.CUSTOMER.getValue()));
        if (!isCustomer) {
            throw new IllegalArgumentException("User is not registered as customer");
        }
        boolean userOfSameOrganization = user.getOrganizations().stream().anyMatch((organization -> organization.getId().equals(orgId)));
        if (!userOfSameOrganization) {
            throw new IllegalArgumentException("User is not belong to expected organization");
        }
        List<VideoKYC> videoKYCList = videoKYCRepository.findAllByOrganizationAndCustomerIdOrderByIdDesc(organizationById.get(), user.getId());
        return videoKYCList;
    }
}
