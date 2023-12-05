package com.example.websonserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "hoa_don")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDon extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_hoa_don")
    private Long maHoaDon;

    @Column(name = "ngay_nhan")
    private LocalDateTime ngayNhan;

    @Column(name = "ngay_thanh_toan")
    private LocalDateTime ngayThanhToan;

    @Column(name = "ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Column(name = "tien_sau_khi_giam_gia")
    private BigDecimal tienGiam;

    @Column(name = "tong_tien")
    private BigDecimal tongTien;

    @Column(name = "tinh")
    private String tinh;

    @Column(name = "huyen")
    private String huyen;

    @Column(name = "xa")
    private String xa;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "ma_phuong_thuc_thanh_toan")
    @JsonIgnore
    private PhuongThucThanhToan phuongThucThanhToan;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung nguoiDung;

    @Column(name = "nhan_vien")
    private Long nhanVien;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<HoaDonChiTiet> hoaDonChiTietList = new ArrayList<>();

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<VoucherChiTiet> voucherChiTiets = new ArrayList<>();

    @Column(name = "trang_thai")
    private Integer trangThai; //Trạng thái giao hàng

    @Column(name = "thanh_toan")
    private Integer thanhToan; //Trạng thái thanh toán

    @Column(name = "xoa")
    private Boolean xoa;

}
