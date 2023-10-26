package com.example.websonserver.api;

import com.example.websonserver.entity.GioHangChiTiet;
import com.example.websonserver.service.GioHangChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/gio-hang-chi-tiet")
public class GioHangChiTietApi {
    @Autowired
    private GioHangChiTietService gioHangChiTietService;

    @PostMapping("/add")
    public ResponseEntity<?> addProductToCart(
            @RequestParam("gioHangId") String gioHangId,
            @RequestParam("SPCTId") String SPCTId,
            @RequestParam("soLuong") String soLuong
    ) {
        try {
            GioHangChiTiet ghct = gioHangChiTietService.addProductToCart(gioHangId, SPCTId, soLuong);
            return ResponseEntity.ok(ghct);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }
}
