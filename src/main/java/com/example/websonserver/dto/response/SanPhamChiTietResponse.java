package com.example.websonserver.dto.response;

import com.example.websonserver.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SanPhamChiTietResponse {
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

     String moTa;

     List<SanPhamTheoThuongHieuResponse> thuongHieuList;

 }
