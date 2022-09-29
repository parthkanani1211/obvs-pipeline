package ai.obvs.services.impl;

import ai.obvs.dto.Response;
import ai.obvs.services.KYCDataExtractionService;
import ai.obvs.services.KYCDataVerificationService;
import ai.obvs.services.RESTService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.StringUtils;

import java.nio.file.Paths;

public class KYCDataExtractionServiceImpl implements KYCDataExtractionService {

    private static final String URL_PREFIX = "http://ec2-44-228-49-156.us-west-2.compute.amazonaws.com:3456";

    private RESTService restService;

    public KYCDataExtractionServiceImpl(RESTService restService) {
        this.restService = restService;
    }

    @Override
    public String getExtractedData(String urlSuffix, String imageFilePath) {
        String url = URL_PREFIX + urlSuffix;
        Response response = restService.postMultipartData(url, imageFilePath);
        return response.getData();
    }
}
