package com.example.websonserver.service;

import com.example.websonserver.dto.request.HoaDonRequest;
import com.example.websonserver.dto.request.ThanhToanRequest;

import java.io.UnsupportedEncodingException;

public interface VnPayService {
     HoaDonRequest creatPayment() throws UnsupportedEncodingException;
}
