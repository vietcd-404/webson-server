package com.example.websonserver.dto.request;

import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.SanPham;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SanPhamRequest {
    private Integer maSanPham;

    @NotBlank(message = "Không bỏ trống tên sản phẩm")
    @NotNull
    private String tenSanPham;
    private Integer doBong;
    private Integer doLi;

    private Boolean xoa = false;

    private Integer trangThai=1;


    public SanPham map(SanPham sanPham){
        sanPham.setTenSanPham(this.getTenSanPham());
        sanPham.setDoBong(this.getDoBong());
        sanPham.setDoLi(this.getDoLi());
        sanPham.setTrangThai(this.getTrangThai());
        sanPham.setXoa(this.getXoa());
        return sanPham;
    }
}
