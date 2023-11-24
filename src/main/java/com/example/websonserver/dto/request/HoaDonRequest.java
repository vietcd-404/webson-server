package com.example.websonserver.dto.request;

import com.example.websonserver.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonRequest {
    private Long maHoaDon;
    private Long maSanPhamCT;
    private LocalDateTime ngayNhan;
    private LocalDateTime ngayThanhToan;

    public Long tongTien1;
    public String moTa ="  ";

    @NotBlank(message = "Không bỏ trong tên người nhận")
    private String tenNguoiNhan;

    private BigDecimal tienGiam;
    private BigDecimal tongTien;

    private String tinh;

    private String huyen;

    private String xa;

    @NotBlank(message = "Không bỏ trống địa chỉ")
    private String diaChi;

    private String email;


    private String nguoiDung;

    @NotNull(message = "Không bỏ trống phương thức thanh toán")
    private String tenPhuongThuc;

    private String tenVoucher;

    @NotNull(message = "Số điện thoại không được để trống")
    private String sdt;

    private Integer thanhToan;

    private Integer soLuong;
    private List<HoaDonChiTiet> hoaDonChiTietList ;
    private List<SanPhamChiTiet> sanPhamChiTiets;
    private List<VoucherChiTiet> voucherChiTiets = new ArrayList<>();
    private Integer trangThai =0;
    private Boolean xoa = false;


}
