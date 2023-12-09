package com.example.websonserver.dto.response;

import com.example.websonserver.entity.AnhSanPham;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DanhSachYTResponse {

    private Long maSanPhamCT;

    private BigDecimal donGia;

    private Integer soLuong;

    private Integer phanTramGiam;

    private String tenSanPham;

    private String tenLoai;

    private String tenThuongHieu;

    private String tenMau;

    private String anh;
}
