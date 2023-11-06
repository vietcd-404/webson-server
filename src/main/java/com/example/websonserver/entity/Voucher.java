package com.example.websonserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "voucher")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Voucher extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_voucher")
    private Long maVoucher;

    @Column(name = "dieu_kien")
    private BigDecimal dieuKien;

    @Column(name = "gia_tri_giam")
    private BigDecimal giaTriGiam;

    @Column(name = "giam_toi_da")
    private BigDecimal giamToiDa;

    @Column(name = "kieu_giam_gia")
    private String kieuGiamGia;

    @Column(name = "voucher")
    private String tenVoucher;

    @Column(name = "thoi_gian_bat_dau")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc")
    private LocalDateTime thoiGianKetThuc;

    @Column(name = "so_luong")
    private Integer soLuong;

    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<VoucherChiTiet> voucherChiTiets = new ArrayList<>();

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "trang_thai")
    private int trangThai;

    @Column(name = "xoa")
    private Boolean xoa;

}
