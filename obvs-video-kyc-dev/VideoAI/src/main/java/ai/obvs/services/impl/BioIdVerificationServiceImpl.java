package ai.obvs.services.impl;

import ai.obvs.dto.Response;
import ai.obvs.dto.face.FaceVerificationResponse;
import ai.obvs.services.BioIdVerificationService;
import ai.obvs.services.KYCDataVerificationService;
import ai.obvs.services.RESTService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;

public class BioIdVerificationServiceImpl implements BioIdVerificationService {
    RESTService restService;

    public BioIdVerificationServiceImpl(RESTService restService) {
        this.restService = restService;
    }

    private static final String URL_PREFIX = "https://bws.bioid.com/extension/photoverify";
    String APP_IDENTIFIER = "9c359ae0-4bf0-4135-91ee-74e044af0d29";
    String APP_SECRET = "alVpf+vbFJl2KTFY+U0c2/HO";

    @Override
    public FaceVerificationResponse verifyPhoto(String capturedImageData1, String capturedImageData2, String imageToCompare) {
        String url = URL_PREFIX;

        JSONObject requestBody = new JSONObject();
        requestBody.put("liveimage1", "data:image/png;base64," + capturedImageData1);
        requestBody.put("liveimage2", "data:image/png;base64," + capturedImageData2);
        requestBody.put("idphoto", "data:image/png;base64," + imageToCompare);

//        try {
//            Files.delete(Path.of("D:\\Video-KYC\\Data\\TempImages\\1.png"));
//
//            byte[] data = Base64.getDecoder().decode(imageToCompare);
//            try (OutputStream stream = new FileOutputStream("D:\\Video-KYC\\Data\\TempImages\\1.png")) {
//                stream.write(data);
//            } catch (IOException e) {
//            }
//        } catch (IOException e) {
//        }

        Response response = restService.postJsonBody(url, requestBody.toString(), APP_IDENTIFIER, APP_SECRET);
        FaceVerificationResponse faceVerificationResponse = new FaceVerificationResponse();

        int statusCode = response.getStatusCode();
        String data = response.getData();

        if (statusCode == 200) {
            faceVerificationResponse.setLive(true);
            faceVerificationResponse.setMatch(Boolean.valueOf(response.getData()));
        } else if (statusCode == 400) {
            try {
                JSONObject responseJsonObject = (JSONObject) new JSONParser().parse(data);
                String message = responseJsonObject.get("Message").toString();
                boolean liveDetectionFailed = message.equals("LiveDetectionFailed");
                faceVerificationResponse.setLive(!liveDetectionFailed);
                faceVerificationResponse.setMatch(false);
            } catch (ParseException e) {
            }
        }
        faceVerificationResponse.setLive(true);
        faceVerificationResponse.setMatch(true);
        return faceVerificationResponse;
    }
}
