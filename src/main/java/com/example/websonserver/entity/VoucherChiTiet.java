package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Table(name = "voucher_chi_tiet")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherChiTiet extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_voucher_chi_tiet")
    private Long  maVoucherCT;

    @Column(name = "so_tien_con_lai")
    private BigDecimal soTienConLai;

    @ManyToOne
    @JoinColumn(name = "ma_voucher")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "ma_hoa_don")
    private HoaDon hoaDon;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "xoa")
    private Boolean xoa;
}
