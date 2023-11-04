package com.example.websonserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThanhToanRequest {
    private Long maHoaDon;
    private Integer tongTien;
    private String moTa;
    private String status;
    private String message;
    private String url;
}
