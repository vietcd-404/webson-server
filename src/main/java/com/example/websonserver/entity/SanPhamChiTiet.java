package com.example.websonserver.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@Table(name = "san_pham_chi_tiet")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamChiTiet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_san_pham_chi_tiet")
    private Long maSanPhamCT;

    @Column(name = "gia_ban")
    private DecimalFormat tenSanPham;

    @Column(name = "gia_nhap")
    private DecimalFormat doBong;

    @Column(name = "so_luong_ton")
    private Integer soLuongTon;

    @ManyToOne
    @JoinColumn(name = "ma_san_pham")
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "ma_loai")
    private Loai loai;

    @ManyToOne
    @JoinColumn(name = "ma_thuong_hieu")
    private ThuongHieu thuongHieu;

    @ManyToOne
    @JoinColumn(name = "ma_mau")
    private MauSac mauSac;

    @ManyToOne
    @JoinColumn(name = "ma_anh")
    private AnhSanPham anhSanPham;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "xoa")
    private Boolean xoa;
}
