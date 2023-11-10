package com.example.websonserver.dto.request;

import com.example.websonserver.entity.Voucher;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class VoucherRequest {
    private Long maVoucher;

    @DecimalMin(value = "0", message = "Không thể giảm dưới 0")
    private BigDecimal giamToiDa;

    private BigDecimal giaTriGiam;

    private String kieuGiamGia;

    private String tenVoucher;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime thoiGianBatDau;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime thoiGianKetThuc;


    private Integer soLuong;


    private String moTa;


    private int trangThai=0;

    private Boolean xoa=false;

    public Voucher map(Voucher voucher){
        voucher.setGiamToiDa(this.getGiamToiDa());
        voucher.setTenVoucher(this.getTenVoucher());
        voucher.setKieuGiamGia(this.getKieuGiamGia());
        voucher.setGiaTriGiam(this.getGiaTriGiam());
        voucher.setThoiGianBatDau(this.getThoiGianBatDau());
        voucher.setThoiGianKetThuc(this.getThoiGianKetThuc());
        voucher.setSoLuong(this.getSoLuong());
        voucher.setMoTa(this.getMoTa());
        voucher.setTrangThai(this.getTrangThai());
        voucher.setXoa(this.getXoa());
        return voucher;
    }
}
