package com.example.websonserver.dto.response;

import com.example.websonserver.entity.HoaDonChiTiet;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.entity.PhuongThucThanhToan;
import com.example.websonserver.entity.VoucherChiTiet;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long maHoaDonCT;

    private LocalDateTime ngayNhan;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss a")
    private LocalDateTime ngayTao;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss a")
    private LocalDateTime ngayThanhToan;

    private String tenNguoiNhan;

    private BigDecimal tienGiam;

    private BigDecimal phiShip;

    private BigDecimal tongTien;

    private String diaChi;

    private String tinh;

    private String huyen;

    private String xa;

    private String sdt;

    private String email;

    private String diaChiChiTiet;

    private String tenPhuongThucThanhToan;

    private String tenNguoiDung;

    private Integer trangThai; //Trạng thái giao hàng

    private Integer thanhToan; //Trạng thái thanh toán

    private NguoiDung nguoiDung;

    private Boolean xoa;

    private String tenNhanVien;

    private List<HoaDonChiTietResponse> hoaDonChiTiet;
}
