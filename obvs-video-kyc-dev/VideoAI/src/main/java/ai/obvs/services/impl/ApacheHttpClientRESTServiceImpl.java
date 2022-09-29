package ai.obvs.services.impl;

import ai.obvs.dto.Response;
import ai.obvs.services.RESTService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.ByteArrayResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;

public class ApacheHttpClientRESTServiceImpl implements RESTService {

    @Override
    public Response postJsonBody(String url, String jsonBodyData) {
        Response response = new Response();
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            StringEntity entity = new StringEntity(jsonBodyData);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse httpResponse = client.execute(httpPost);
            InputStream contentStream = httpResponse.getEntity().getContent();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String data = IOUtils.toString(contentStream, StandardCharsets.UTF_8.name());
            client.close();
            response.setStatusCode(statusCode);
            response.setData(data);
        } catch (IOException e) {
        }
        return response;
    }

    @Override
    public Response postJsonBody(String url, String token, String jsonBodyData) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            StringEntity entity = new StringEntity(jsonBodyData);
            httpPost.setEntity(entity);
            httpPost.setHeader("Authorization", "Bearer " + token);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse httpResponse = client.execute(httpPost);
            InputStream contentStream = httpResponse.getEntity().getContent();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String data = IOUtils.toString(contentStream, StandardCharsets.UTF_8.name());
            client.close();
            Response response = new Response();
            response.setStatusCode(statusCode);
            response.setData(data);
            return response;
        } catch (IOException e) {
        }
        return new Response();
    }

    @Override
    public Response postMultipartData(String url, String filePath) {
        Response response = new Response();
        try {
            String fileName = Paths.get(filePath).getFileName().toString();

            CloseableHttpClient client = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(url);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody(
                    "file", new File(filePath), ContentType.APPLICATION_OCTET_STREAM, fileName);

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);

            CloseableHttpResponse httpResponse = client.execute(httpPost);
            InputStream contentStream = httpResponse.getEntity().getContent();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String data = IOUtils.toString(contentStream, StandardCharsets.UTF_8.name());
            client.close();
            response.setStatusCode(statusCode);
            response.setData(data);
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        return response;
    }

    @Override
    public Response postJsonBody(String url, String jsonBodyData, String user, String password) {
        Response response = new Response();
        HttpPost httpPost = new HttpPost(url);

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(user, password)
        );
        try {
            StringEntity entity = new StringEntity(jsonBodyData);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
        } catch (UnsupportedEncodingException e) {
        }

        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
             CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            InputStream contentStream = httpResponse.getEntity().getContent();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String data = IOUtils.toString(contentStream, StandardCharsets.UTF_8.name());
            response.setStatusCode(statusCode);
            response.setData(data);
        } catch (IOException e) {
        }
        return response;
    }

    @Override
    public Response get(String url, String username, String password) {
        HttpGet httpGet = new HttpGet(url);
        Response response = new Response();

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(username, password)
        );

        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
             CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
//            InputStream contentStream = ;
//            ByteArrayInputStream targetStream = new ByteArrayInputStream(contentStream.readAllBytes());
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            byte[] bytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
            response.setStatusCode(statusCode);
            response.setContent(bytes);

        } catch (IOException e) {
            System.out.println("Error" + e);
        }
//        writeByte(response.getContent());

        return response;
    }

    static void writeByte(byte[] bytes) {
        try {
            OutputStream os = new FileOutputStream(new File("D:\\Temp\\1.mp4"));

            os.write(bytes);

            os.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
