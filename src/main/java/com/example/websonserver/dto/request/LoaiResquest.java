package com.example.websonserver.dto.request;

import com.example.websonserver.entity.BaseEntity;
import com.example.websonserver.entity.Loai;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class LoaiResquest  {
    private Integer maLoai;

    @NotBlank(message = "Không bỏ trống tên loại")
    @NotNull
    private String tenLoai;

    private Boolean xoa = false;

    private Integer trangThai=1;


    public Loai map(Loai loai){
        loai.setTenLoai(this.getTenLoai());
        loai.setTrangThai(this.getTrangThai());
        loai.setXoa(this.getXoa());
        return loai;
    }
}
