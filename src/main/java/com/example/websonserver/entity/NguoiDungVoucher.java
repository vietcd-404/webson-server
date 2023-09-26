package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Table(name = "voucher_nguoi_dung")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NguoiDungVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_voucher_nguoi_dung")
    private Long maNguoiDungVoucher;

    @Column(name = "dieu_kien")
    private DecimalFormat dieuKien;

    @Column(name = "thoi_gian_ap_dung")
    private LocalDateTime thoiGianApDung;

        @ManyToOne
        @JoinColumn(name = "ma_voucher")
        private Voucher voucher;

        @ManyToOne
        @JoinColumn(name = "ma_nguoi_dung")
        private NguoiDung nguoiDung;


}
