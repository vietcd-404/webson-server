package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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
    private BigDecimal giaBan;

    @Column(name = "phan_tram_giam")
    private Integer phanTramGiam;

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

    @OneToMany(mappedBy = "sanPhamChiTiet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnhSanPham> anhSanPhamList;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "xoa")
    private Boolean xoa;
}
