package com.example.websonserver.dto.response;

import com.example.websonserver.entity.AnhSanPham;
import com.example.websonserver.entity.SanPhamChiTiet;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class GioHangChiTietResponse {
    private BigDecimal donGia;

    private Integer soLuong;

    private Integer phanTramGiam;

    private String tenSanPham;

    private String tenLoai;

    private String tenThuongHieu;

    private String tenMauSac;

    private List<String> anh;
}
