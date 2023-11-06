package com.example.websonserver.dto.response;

import com.example.websonserver.entity.AnhSanPham;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamChiTietRes {
    Long maSanPhamCT;

    BigDecimal giaBan;

    Integer phanTramGiam;

    Integer soLuongTon;

    String tenSanPham;

    String tenLoai;

    String tenThuongHieu;

    String tenMau;

    Integer trangThai;

    List<String> danhSachAnh;

}
