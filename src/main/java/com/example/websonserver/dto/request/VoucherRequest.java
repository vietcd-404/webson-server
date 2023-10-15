package com.example.websonserver.dto.request;

import com.example.websonserver.entity.Voucher;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class VoucherRequest {
    private Long maVoucher;

    @DecimalMin(value = "0", message = "Không thể giảm dưới 0")
    private BigDecimal giamToiDa;

    private String tenVoucher;


    private LocalDateTime thoiGianBatDau;


    private LocalDateTime thoiGianKetThuc;


    private Integer soLuong;


    private String moTa;


    private int trangThai=0;

    private Boolean xoa=false;

    public Voucher map(Voucher voucher){
        voucher.setGiamToiDa(this.getGiamToiDa());
        voucher.setTenVoucher(this.getTenVoucher());
        voucher.setThoiGianBatDau(this.getThoiGianBatDau());
        voucher.setThoiGianKetThuc(this.getThoiGianKetThuc());
        voucher.setSoLuong(this.getSoLuong());
        voucher.setMoTa(this.getMoTa());
        voucher.setTrangThai(this.getTrangThai());
        voucher.setXoa(this.getXoa());
        return voucher;
    }
}
