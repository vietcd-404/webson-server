package com.example.websonserver.dto.response;

import com.example.websonserver.constants.Constants;
import com.example.websonserver.dto.request.ThanhToanRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String message;

    public static MessageResponse warning(String mess) {
        return new MessageResponse(mess);
    }

    public static MessageResponse success(String mess) {
        return new MessageResponse(mess);
    }
    public static MessageResponse url(String url) {
        return new MessageResponse(url);
    }


    public ThanhToanRequest withData(ThanhToanRequest request) {
        return request;
    }
}
