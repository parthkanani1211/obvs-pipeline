package ai.obvs.services.impl;

import ai.obvs.dto.AppDto;
import ai.obvs.dto.LocationRequestDto;
import ai.obvs.dto.LocationResponseDto;
import ai.obvs.mapper.AppMapper;
import ai.obvs.model.App;
import ai.obvs.repository.AppRepository;
import ai.obvs.services.AppService;
import ai.obvs.services.GeoLocationService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
public class GeoLocationServiceImpl implements GeoLocationService {

//    @Value("ai.server.url")
    private String url;

    @Override
    public LocationResponseDto getLocation(LocationRequestDto locationRequestDto) {
        String locationValue = "";
        try {
            locationValue = getCloseableHttpResponse(locationRequestDto);
        } catch (IOException e) {
        }
        LocationResponseDto locationResponseDto = new LocationResponseDto();
        locationResponseDto.setLocation(locationValue);
        return locationResponseDto;
    }

    private String getCloseableHttpResponse(LocationRequestDto locationRequestDto) throws IOException {
        url = "https://us1.locationiq.com/v1/reverse.php?key=74a2b2de4d2001&lat=%s&lon=%s&format=json";
        url = String.format(this.url, locationRequestDto.getLatitude(), locationRequestDto.getLongitude());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(this.url);
        CloseableHttpResponse response = client.execute(httpGet);
        InputStream inputStream = response.getEntity().getContent();
        StringWriter writer = new StringWriter();
        String encoding = StandardCharsets.UTF_8.name();
        IOUtils.copy(inputStream, writer, encoding);
        String location = writer.toString();
        return location;
    }
}
