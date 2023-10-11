package com.example.websonserver.dto.request;

import com.example.websonserver.entity.DiaChi;
import com.example.websonserver.entity.NguoiDung;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DiaChiRequest {
    @NotBlank(message = "Không bỏ trống dịa chỉ")
    @NotNull
    private String diaChi;

    @NotBlank(message = "Không bỏ trống loại địa chỉ")
    @NotNull
    private String loaiDiaChi;

    @NotBlank(message = "Không bỏ trống mã người dùng")
    @NotNull
    private String maNguoiDung;

    private Boolean xoa = false;

    private Integer trangThai=0;

    public DiaChi map(DiaChi dc){
        dc.setDiaChi(this.getDiaChi());
        dc.setLoai_dia_chi(this.getLoaiDiaChi());
        dc.setNguoiDung(NguoiDung.builder().maNguoiDung(Long.parseLong(this.getMaNguoiDung())).build());
        dc.setTrangThai(this.getTrangThai());
        dc.setXoa(this.getXoa());
        return dc;
    }
}
