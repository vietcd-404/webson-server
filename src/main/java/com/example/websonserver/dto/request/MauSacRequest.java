package com.example.websonserver.dto.request;

import com.example.websonserver.entity.MauSac;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MauSacRequest {
    private Integer maMau;

    @NotBlank(message = "Không bỏ trống tên màu")
    @NotNull
    private String tenMau;

    private Boolean xoa = false;
    private Integer trangThai=0;


    public MauSac map(MauSac mauSac){
        mauSac.setTenMau(this.getTenMau());
        mauSac.setTrangThai(this.getTrangThai());
        mauSac.setXoa(this.getXoa());
        return mauSac;
    }
}
