package com.example.websonserver.dto.response;

import com.example.websonserver.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamChiTietResponse {
    private Long maSanPhamCT;

    private BigDecimal giaBan;

    private Integer phanTramGiam;

    private Integer soLuongTon;

    private String tenSanPham;

    private String tenLoai;

    private String tenThuongHieu;

    private String tenMau;

    private List<AnhSanPham> danhSachAnh;

    private Integer trangThai;

//    private Boolean xoa;
}
