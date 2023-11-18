package com.example.websonserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHoaDonRequest {
    private Long maHoaDon;
    private  Long maHoaDonCT;
    private String huyen;
    private String tinh;
    private String xa;
    private String sdt;
    private String tenNguoiNhan;
    private Integer soLuong;
    private Integer trangThai;
    private BigDecimal tongTien;
    private String diaChi;
}
