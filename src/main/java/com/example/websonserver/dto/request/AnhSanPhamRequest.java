package com.example.websonserver.dto.request;

import com.example.websonserver.entity.AnhSanPham;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnhSanPhamRequest {
    private Integer maAnh;

    @NotBlank(message = "Không bỏ trống ảnh")
    @NotNull
    private String anh;

    private Boolean xoa = false;

    private Integer trangThai=0;

    public AnhSanPham map(AnhSanPham anhSanPham){
//        anhSanPham.setAnh(this.getAnh());
        anhSanPham.setTrangThai(this.getTrangThai());
        anhSanPham.setXoa(this.getXoa());
        return anhSanPham;
    }
}
