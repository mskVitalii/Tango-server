package com.tango.utils;

import com.tango.DTO.ImgurResponse;
import com.tango.exception.TangoException;
import org.apache.commons.io.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ImgurResponseHandler implements ResponseHandler {

    @Override
    public ImgurResponse handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
        int status = response.getStatusLine().getStatusCode();
        ImgurResponse rspObject = new ImgurResponse();
        rspObject.setStatusCode(status);

        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            Header headerEncoding = response.getEntity().getContentEncoding();

//            System.out.println("Printing Response Header...\n");
//            Header[] headers = response.getAllHeaders();
//            for (Header header : headers) {
//                System.out.println("Key : " + header.getName() + " ,Value : " + header.getValue());
//            }
//            System.out.println("X-RateLimit-UserLimit: " + response.getFirstHeader("X-RateLimit-UserLimit").getValue());
//            System.out.println("X-RateLimit-UserRemaining: " + response.getFirstHeader("X-RateLimit-UserRemaining").getValue());
//            System.out.println("X-RateLimit-UserReset: " + response.getFirstHeader("X-RateLimit-UserReset").getValue());
//            System.out.println("X-RateLimit-ClientLimit: " + response.getFirstHeader("X-RateLimit-ClientLimit").getValue());
//            System.out.println("X-RateLimit-ClientRemaining: " + response.getFirstHeader("X-RateLimit-ClientRemaining").getValue());
//            System.out.println("X-Post-Rate-Limit-Reset: " + response.getFirstHeader("X-Post-Rate-Limit-Reset").getValue());
//            System.out.println("X-Post-Rate-Limit-Remaining: " + response.getFirstHeader("X-Post-Rate-Limit-Remaining").getValue());
//            System.out.println("X-Post-Rate-Limit-Limit: " + response.getFirstHeader("X-Post-Rate-Limit-Limit").getValue());


            Charset enocodedCharset = (headerEncoding == null) ? StandardCharsets.UTF_8 : Charsets.toCharset(headerEncoding.toString());
            String jsonResponse = EntityUtils.toString(entity, enocodedCharset);
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONObject dataObject = (JSONObject) jsonObject.get("data");
                System.out.println("link is" + dataObject.get("link"));
                System.out.println("response: " + dataObject.toString());
                rspObject.setLink((String) dataObject.get("link"));
                rspObject.setId((String) dataObject.get("id"));
                rspObject.setImgType((String) dataObject.get("type"));
            } catch (JSONException e) {
                System.out.println(e.getMessage());
                throw new TangoException("[ERROR] Something wrong with JSON\ndetails:\t" + e.getMessage());
            }

        }

        return rspObject;
    }
}
