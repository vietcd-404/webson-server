//package com.example.websonserver.api;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
//        // Xử lý ngoại lệ và trả về thông báo lỗi
//        String errorMessage = e.getMessage(); // Lấy thông báo lỗi từ ngoại lệ
//        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST); // Trả về lỗi HTTP 400 Bad Request
//    }
//}