package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@Table(name = "gio_hang")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GioHangChiTiet extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_gio_hang_chi_tiet")
    private Long ma;

    @Column(name = "don_gia")
    private DecimalFormat donGia;

    @Column(name = "so_luong")
    private Integer soLuong;

    @ManyToOne
    @JoinColumn(name = "gio_hang")
    private GioHang gioHang;

    @ManyToOne
    @JoinColumn(name = "ma_san_pham_chi_tiet")
    private SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "xoa")
    private Boolean xoa;


}
