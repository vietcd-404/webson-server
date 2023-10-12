package com.example.websonserver.dto.request;

import com.example.websonserver.entity.KhuyenMai;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class KhuyenMaiRequest {
    private Long maKhuyenMai;

    @DecimalMin(value = "0", message = "Điều kiện không được nhỏ hơn 0 phần trăm")
    private BigDecimal dieuKien;

    private BigDecimal giaTriGiam;

    private BigDecimal giamToiDa;

    private String kieuGiamGia;

    private String moTa;

    @Min(value = 1,message = "Số lượng không được nhỏ hơn 1")
    private Integer soLuong;

    private String khuyenMai;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String thoiGianBatDau;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String thoiGianKetThuc;

    private Boolean xoa = false;

    private Integer trangThai=0;

    public KhuyenMai map(KhuyenMai km){
        km.setDieuKien(this.getDieuKien());
        km.setGiaTriGiam(this.getGiaTriGiam());
        km.setGiamToiDa(this.getGiamToiDa());
        km.setKieuGiamGia(this.getKieuGiamGia());
        km.setMoTa(this.getMoTa());
        km.setSoLuong(this.getSoLuong());
        km.setKhuyenMai(this.getKhuyenMai());
        km.setThoiGianBatDau(LocalDateTime.parse(this.getThoiGianBatDau()));
        km.setThoiGianKetThuc(LocalDateTime.parse(this.getThoiGianKetThuc()));
        km.setTrangThai(this.getTrangThai());
        km.setXoa(this.getXoa());
        return km;
    }
}
