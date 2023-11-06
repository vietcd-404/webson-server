package com.example.websonserver.service;

import com.example.websonserver.dto.request.ThanhToanRequest;

import java.io.UnsupportedEncodingException;

public interface VnPayService {
     ThanhToanRequest creatPayment() throws UnsupportedEncodingException;
}
