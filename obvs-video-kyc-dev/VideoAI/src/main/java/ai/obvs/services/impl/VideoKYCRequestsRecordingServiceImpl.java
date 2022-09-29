package ai.obvs.services.impl;

import ai.obvs.Enums.Roles;
import ai.obvs.dto.Response;
import ai.obvs.model.VideoSession;
import ai.obvs.security.CurrentUser;
import ai.obvs.security.UserPrincipal;
import ai.obvs.services.RESTService;
import ai.obvs.services.VideoKYCRequestsRecordingService;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Recording;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class VideoKYCRequestsRecordingServiceImpl implements VideoKYCRequestsRecordingService {

    private OpenViduService openViduService;
    private RESTService restService;

    // URL where our OpenVidu server is listening
    private String USER_NAME;
    // Secret shared with our OpenVidu server
    private String SECRET;


    public VideoKYCRequestsRecordingServiceImpl(OpenViduService openViduService, RESTService restService) { //@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl
        this.openViduService = openViduService;
        this.restService = restService;

        this.USER_NAME = "OPENVIDUAPP";
        this.SECRET = "obvs!1234";
    }

    @Override
    public Response downloadRecording(VideoSession videoSession) throws OpenViduJavaClientException, OpenViduHttpException {
        Recording recording = openViduService.getRecording(videoSession.getSession());
        if (recording.getStatus() == Recording.Status.ready)
            return restService.get(recording.getUrl(), USER_NAME, SECRET);

        throw new RuntimeException("Recording is not available yet. Please try after sometime.");
    }
}
