package com.example.websonserver.api;


import com.example.websonserver.dto.response.ThanhToanRes;
import com.example.websonserver.service.serviceIpml.VnPayServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/vnpay")
public class VNPayApi {
    @Autowired
    private VnPayServiceImpl vnPayService;

    @PostMapping("/pay")
    public String pay(@RequestBody ThanhToanRes payModel, HttpServletRequest request) {
        try {
            return vnPayService.payWithVNPAY(payModel, request);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


}
