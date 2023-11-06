package com.example.websonserver.dto.response;

import com.example.websonserver.entity.HoaDonChiTiet;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.entity.PhuongThucThanhToan;
import com.example.websonserver.entity.VoucherChiTiet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonResponse {

    private Long maHoaDon;

    private LocalDateTime ngayNhan;

    private LocalDateTime ngayThanhToan;

    private String tenNguoiNhan;

    private BigDecimal tienGiam;

    private BigDecimal tongTien;

    private String diaChi;

    private String sdt;

    private String tenPhuongThucThanhToan;

    private String tenNguoiDung;

    private Integer trangThai; //Trạng thái giao hàng

    private Integer thanhToan; //Trạng thái thanh toán

    private Boolean xoa;
}