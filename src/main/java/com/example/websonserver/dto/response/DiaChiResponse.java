package com.example.websonserver.dto.response;

import com.example.websonserver.entity.NguoiDung;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaChiResponse {

    private Long maDiaChi;

    private String diaChi;

    private String loaiDiaChi;

    private String sdt;

    private String tinh;

    private String huyen;

    private String xa;

    private Integer trangThai;

    private Boolean xoa;
}
