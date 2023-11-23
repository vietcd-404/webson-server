package com.example.websonserver.api;


import com.example.websonserver.dto.request.HoaDonRequest;
import com.example.websonserver.dto.response.ThanhToanRes;
import com.example.websonserver.dto.response.UrlResponse;
import com.example.websonserver.service.serviceIpml.VnPayServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/vnpay")
public class VNPayApi {
    @Autowired
    private VnPayServiceImpl vnPayService;

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody ThanhToanRes payModel, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(new UrlResponse(vnPayService.payWithVNPAY(payModel, request)));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/callback")
    public String handleVnPayCallback(@RequestParam("vnp_ResponseCode") String responseCode,
                                      @RequestParam("vnp_TxnRef") String transactionRef,
                                      @RequestParam Long maHoaDon) {

        if ("00".equals(responseCode)) {


            return "Payment successful";
        } else {
            // Payment failed, handle accordingly
            return "Payment failed";
        }
    }


}
