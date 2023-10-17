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

     List<String> danhSachAnh;

     Integer trangThai;

//    public SanPhamChiTietResponse map(SanPhamChiTiet sanPhamChiTiet) {
//        SanPhamChiTietResponse response = new SanPhamChiTietResponse();
//        response.setMaSanPhamCT(sanPhamChiTiet.getMaSanPhamCT());
//        response.setTenSanPham(sanPhamChiTiet.getSanPham().getTenSanPham());
//        response.setSoLuongTon(sanPhamChiTiet.getSoLuongTon());
//        response.setPhanTramGiam(sanPhamChiTiet.getPhanTramGiam());
//        response.setGiaBan(sanPhamChiTiet.getGiaBan());
//        response.setTenLoai(sanPhamChiTiet.getLoai().getTenLoai());
//        response.setTenThuongHieu(sanPhamChiTiet.getThuongHieu().getTenThuongHieu());
//        response.setTenMau(sanPhamChiTiet.getMauSac().getTenMau());
//
//        List<String> anhSanPhamList = new ArrayList<>();
//        for (AnhSanPham anhSanPham : sanPhamChiTiet.getAnhSanPhamList()) {
//            anhSanPhamList.add(anhSanPham.getAnh());
//        }
//        response.setDanhSachAnh(anhSanPhamList);
//
//        response.setTrangThai(sanPhamChiTiet.getTrangThai());
//
//
//        return response;
//    }

    public SanPhamChiTietResponse mapToResponse(SanPhamChiTiet sanPhamChiTiet) {
//        SanPhamChiTietResponse dto = new SanPhamChiTietResponse();
//        dto.setMaSanPhamCT(sanPhamChiTiet.getMaSanPhamCT());
//        dto.setGiaBan(sanPhamChiTiet.getGiaBan());
//        dto.setPhanTramGiam(sanPhamChiTiet.getPhanTramGiam());
//        dto.setSoLuongTon(sanPhamChiTiet.getSoLuongTon());
//        dto.setTenSanPham(sanPhamChiTiet.getSanPham().getTenSanPham()); // Lấy tên sản phẩm
//        dto.setTenLoai(sanPhamChiTiet.getLoai().getTenLoai()); // Lấy tên loại
//        dto.setTenThuongHieu(sanPhamChiTiet.getThuongHieu().getTenThuongHieu()); // Lấy tên thương hiệu
//        dto.setTenMau(sanPhamChiTiet.getMauSac().getTenMau()); // Lấy tên màu sắc
//        List<String> imageUrls = anhSanPhamService.getImagesBySanPhamChiTiet(sanPhamChiTiet.getMaSanPhamCT());
//        dto.setImageUrls(imageUrls);
//        sanPhamChiTietDtos.add(dto);

        return null;
    }
 }
