package com.tango.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImgurResponse {
    private String link;
    private String id;
    private String imgType;
    private int statusCode;

    @Override
    public String toString() {
        if (statusCode >= 200 && statusCode < 300)
            return "ResponseObject{" +
                    "link='" + link + '\'' +
                    ", id='" + id + '\'' +
                    ", imgType='" + imgType + '\'' +
                    ", status='" + statusCode + '\'' +
                    '}';
        
        return "ResponseObject{status='" + statusCode + "'}";
    }
}
