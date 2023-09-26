package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.CustomLog;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Table(name = "khuyen_mai")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KhuyenMai extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_khuyen_mai")
    private Long maKhuyenMai;

    @Column(name = "dieu_kien")
    private DecimalFormat dieuKien;

    @Column(name = "gia_tri_giam")
    private DecimalFormat giaTriGiam;

    @Column(name = "giam_toi_da")
    private DecimalFormat giamToiDa;

    @Column(name = "kieu_giam_gia")
    private DecimalFormat kieuGiamGia;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "thoi_gian_bat_dau")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc")
    private LocalDateTime thoiGianKetThuc;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "xoa")
    private Boolean xoa;
}
