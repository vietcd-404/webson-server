package com.example.websonserver.dto.response;

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
}
