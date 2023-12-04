package com.example.websonserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonTaiQuayResquest {
    private Long maHoaDon;
    private Long maHoaDonCT;
    private Long maNguoiDung;
    private String huyen;
    private String tinh;
    private String xa;
    private String sdt;
    private String tenNguoiNhan;
    private Integer soLuong;
    private Integer trangThai = 3;
    private Integer thanhToan;
    private String nhanVien;
    private Boolean xoa = false;
    private BigDecimal tongTien;
    private String diaChi;
}
