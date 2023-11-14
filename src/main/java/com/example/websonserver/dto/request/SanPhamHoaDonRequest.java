package com.example.websonserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamHoaDonRequest {
    private Long maHoaDon;
    private Long maSanPhamCT;
    private Long maHoaDonCT;
    private BigDecimal tongTien;
    private Integer soLuong;
}
