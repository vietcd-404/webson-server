package com.example.websonserver.dto.request;

import com.example.websonserver.entity.HoaDonChiTiet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonChiTietRequest {
    private BigDecimal donGia;
    private Integer soLuong;
    private Long maHoaDonCT;
    private Long maSanPhamCT;
    private List<HoaDonChiTiet> hoaDonChiTietList;

}
