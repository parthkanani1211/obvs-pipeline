package ai.obvs.services;

import ai.obvs.dto.Response;
import ai.obvs.model.VideoSession;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;

public interface VideoKYCRequestsRecordingService {
    Response downloadRecording(VideoSession videoSession) throws OpenViduJavaClientException, OpenViduHttpException;
}
