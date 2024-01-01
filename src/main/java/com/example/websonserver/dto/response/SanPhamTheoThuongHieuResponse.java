package com.example.websonserver.dto.response;

import com.example.websonserver.entity.AnhSanPham;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamTheoThuongHieuResponse {
    Long maSanPhamCT;

    BigDecimal giaBan;

    Integer phanTramGiam;

    Integer soLuongTon;

    String tenSanPham;

    String tenLoai;

    String tenThuongHieu;

    String tenMau;

    List<AnhSanPham> danhSachAnh;

    List<String> img;

    Integer trangThai;
}
