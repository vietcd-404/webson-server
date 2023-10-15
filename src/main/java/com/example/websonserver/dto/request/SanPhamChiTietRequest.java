package com.example.websonserver.dto.request;


import com.example.websonserver.entity.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
@Builder
@Data
public class SanPhamChiTietRequest {
    @NotNull(message = "Không bỏ trống giá bán")
    @Min(value = 1, message = "Giá bán phải lớn hơn 0")
    private BigDecimal giaBan;

    @NotNull(message = "Không bỏ trống giá nhập")
    @Min(value = 1 ,message = "Giá nhập phải lớn hơn 0")
    private BigDecimal giaNhap;

    @NotNull(message = "Không bỏ trống giá bán")
    @Min(value = 1 ,message = "Số lượng tồn phải lớn hơn 0")
    private Integer soLuongTon;

    @NotBlank(message = "Không bỏ trống mã sản phẩm")
    @NotNull
    private String maSP;

    @NotBlank(message = "Không bỏ trống mã loại")
    @NotNull
    private String maLoai;

    @NotBlank(message = "Không bỏ trống mã thương hiệu")
    @NotNull
    private String maThuongHieu;

    @NotBlank(message = "Không bỏ trống mã màu")
    @NotNull
    private String maMau;

    @NotBlank(message = "Không bỏ trống mã ảnh")
    @NotNull
    private String maAnh;

    private Boolean xoa = false;

    private Integer trangThai=0;

    public SanPhamChiTiet map(SanPhamChiTiet sanPhamChiTiet){
        sanPhamChiTiet.setGiaBan(this.getGiaBan());
//        sanPhamChiTiet.setGiaNhap(this.getGiaNhap());
        sanPhamChiTiet.setSoLuongTon(this.getSoLuongTon());
        sanPhamChiTiet.setSanPham(SanPham.builder().maSanPham(Long.parseLong(this.getMaSP())).build());
        sanPhamChiTiet.setThuongHieu(ThuongHieu.builder().maThuongHieu(Long.parseLong(this.getMaThuongHieu())).build());
        sanPhamChiTiet.setMauSac(MauSac.builder().maMau(Long.parseLong(this.getMaMau())).build());
//        sanPhamChiTiet.setAnhSanPham(AnhSanPham.builder().maAnh(Long.parseLong(this.getMaAnh())).build());
        sanPhamChiTiet.setXoa(this.getXoa());
        sanPhamChiTiet.setTrangThai(this.getTrangThai());
        return sanPhamChiTiet;
    }
}
