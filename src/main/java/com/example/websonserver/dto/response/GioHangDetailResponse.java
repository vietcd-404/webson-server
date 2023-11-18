package com.example.websonserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GioHangDetailResponse {
    private Long maGioHang;
    private Long maGH;
    private Integer soLuongTon;
    private Long maSanPhamCT;
    private List<String> anh;
    private String tenSanPham;
    private BigDecimal giaBan;
    private Integer soLuong;
}
