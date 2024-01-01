package com.example.websonserver.dto.request;

import com.example.websonserver.entity.DiaChi;
import com.example.websonserver.entity.NguoiDung;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DiaChiRequest {

    private Long maDiaChi;

    private String diaChi;

    private String loaiDiaChi;

    private String sdt;

    private String tinh;

    private String huyen;

    private String xa;

    private Integer trangThai=0;

    private Boolean xoa=false;

    public DiaChi map(DiaChi dc){
        dc.setDiaChi(this.getDiaChi());
        dc.setLoaiDiaChi(this.getLoaiDiaChi());
//        dc.setNguoiDung(NguoiDung.builder().maNguoiDung(Long.parseLong(this.getMaNguoiDung())).build());
        dc.setTrangThai(this.getTrangThai());
        dc.setXoa(this.getXoa());
        return dc;
    }
}
