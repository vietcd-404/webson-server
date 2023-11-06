package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Table(name = "hoa_don_chi_tiet")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonChiTiet extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_hoa_don_chi_tiet")
    private Long maHDCT;

    @Column(name = "don_gia")
    private BigDecimal donGia;

    @Column(name = "so_luong")
    private Integer soLuong;

    @ManyToOne
    @JoinColumn(name = "ma_hoa_don")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "ma_san_pham_chi_tiet")
    private SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "xoa")
    private Boolean xoa;
}
