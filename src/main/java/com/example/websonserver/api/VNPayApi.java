package com.example.websonserver.api;


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
    public ResponseEntity<?> pay(ThanhToanRes payModel, @RequestParam Long tongTien, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(new UrlResponse(vnPayService.payWithVNPAY(payModel, tongTien, request)));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/thanh-toan")
    public ResponseEntity<?> ThanhToan(@RequestParam Long maHoaDon) {
        return ResponseEntity.ok(vnPayService.vnpayReturn(maHoaDon));
    }


}
