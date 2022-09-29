package ai.obvs.services;

import ai.obvs.dto.Response;

import java.util.Map;

public interface RESTService {
    Response postJsonBody(String url, String jsonBodyData);
    Response postJsonBody(String url, String token, String jsonBodyData);
    Response postMultipartData(String url, String filePath);
    Response postJsonBody(String url, String jsonBodyData, String user, String password);

    Response get(String url, String username, String password);
}
