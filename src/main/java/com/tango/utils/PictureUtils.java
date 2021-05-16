package com.tango.utils;

import com.tango.DTO.ImgurResponse;
import com.tango.exception.TangoException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PictureUtils {

    public static final String IMGUR_URL = "https://api.imgur.com/3/image";
    @Value("${imgur.client-id}")
    private String CLIENT_ID;
//    @Value("${imgur.client-secret}")
//    private String clientSecret;

    public String postPictureToImgur(String pictureBase64) {
        // Параметры запроса
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("image", pictureBase64));

        try {
            // Настройка запроса
            var httpPostRequest = new HttpPost(IMGUR_URL);
            httpPostRequest.setHeader("Authorization", "Client-ID " + CLIENT_ID);
            httpPostRequest.setEntity(new UrlEncodedFormEntity(params));

            // Отправка запроса
            CloseableHttpClient httpClient = HttpClients.createDefault();
            var responseBody = (ImgurResponse) httpClient.execute(httpPostRequest, new ImgurResponseHandler());
            httpClient.close();

            System.out.println("----------------------------------------");
            System.out.println(responseBody);

            int status = responseBody.getStatusCode();

            if (status >= 200 && status < 300) {
                System.out.println("SUCCESS UPLOADING.");
                return responseBody.getLink();
            }
            System.out.println("FAILURE UPLOADING.");
            throw new TangoException("FAILURE UPLOADING.");
        } catch (IOException e) {
            throw new TangoException("\n[ERROR]\tUPLOADING FAILURE.\n" + e.getMessage(), e);
        }
    }
}
