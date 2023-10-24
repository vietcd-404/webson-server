package com.example.websonserver.dto.request;


import com.example.websonserver.entity.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class SanPhamChiTietRequest {
    @NotNull(message = "Không bỏ trống giá bán")
    @Min(value = 1, message = "Giá bán phải lớn hơn 0")
    private BigDecimal giaBan;

    @NotNull(message = "Không bỏ phần trăm giảm")
    @Min(value = 1 ,message = "Phần trăm giảm phải lớn hơn 0")
    private Integer phanTramGiam;

    @NotNull(message = "Không bỏ trống giá bán")
    @Min(value = 1 ,message = "Số lượng tồn phải lớn hơn 0")
    private Integer soLuongTon;

    @NotBlank(message = "Không bỏ trống tên sản phẩm")
    @NotNull
    private String tenSanPham;

    @NotBlank(message = "Không bỏ trống tên loại")
    @NotNull
    private String tenLoai;

    @NotBlank(message = "Không bỏ trống tên thương hiệu")
    @NotNull
    private String tenThuongHieu;

    @NotBlank(message = "Không bỏ trống tên màu")
    @NotNull
    private String tenMau;


    private List<Long> danhSachAnh;

    private Boolean xoa = false;

    private Integer trangThai=0;

}
