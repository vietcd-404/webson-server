package com.example.websonserver.dto.response;

import com.example.websonserver.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

 }
