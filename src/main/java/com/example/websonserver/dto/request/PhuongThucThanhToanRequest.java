package com.example.websonserver.dto.request;

import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.PhuongThucThanhToan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PhuongThucThanhToanRequest {
    private Long maPhuongThuc;

    @NotBlank(message = "Không bỏ trống phương thức thanh toán")
    @NotNull
    private String tenPhuongThuc;

    private Boolean xoa = false;

    private Integer trangThai=0;


    public PhuongThucThanhToan map(PhuongThucThanhToan phuongThucThanhToan){
        phuongThucThanhToan.setTenPhuongThuc(this.getTenPhuongThuc());
        phuongThucThanhToan.setTrangThai(this.getTrangThai());
        phuongThucThanhToan.setXoa(this.getXoa());
        return phuongThucThanhToan;
    }
}
