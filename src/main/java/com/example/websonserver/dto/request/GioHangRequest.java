package com.example.websonserver.dto.request;

import com.example.websonserver.entity.GioHang;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.NguoiDung;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GioHangRequest {
    @NotBlank(message = "Không bỏ trống tên người dùng")
    @NotNull
    private String tenNguoiDung;

    private Boolean xoa = false;

    private Integer trangThai=0;
    public GioHang map(GioHang gioHang){
        gioHang.setNguoiDung(NguoiDung.builder().ten(this.getTenNguoiDung()).build());
        gioHang.setTrangThai(this.getTrangThai());
        gioHang.setXoa(this.getXoa());
        return gioHang;
    }
}
