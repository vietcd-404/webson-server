package com.example.websonserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonChiTietResponse {
    private Long maHoaDon;
    private Long maHoaDonCT;
    private Long maSanPhamCT;

    private LocalDateTime ngayTao;

    private String tenNguoiNhan;

    private Integer soLuong;

    private Integer soLuongTon;

    private BigDecimal giaBan;

    private String tenSanPham;

    private BigDecimal tongTien;

    private BigDecimal tienGiam;

    private List<String> anh;

    private String tinh;

    private String huyen;

    private String xa;

    private String diaChi;

    private String sdt;

    private String tenPhuongThucThanhToan;

    private String tenNguoiDung;

    private Integer trangThai; //Trạng thái giao hàng

    private Integer thanhToan; //Trạng thái thanh toán

    private Boolean xoa;
}
