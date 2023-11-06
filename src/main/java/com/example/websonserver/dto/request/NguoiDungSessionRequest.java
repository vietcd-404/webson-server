package com.example.websonserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NguoiDungSessionRequest {
    private String tenNguoiNhan;

    private String sdt;

    private String diaChi;

    private String phuongThucThanhToan;

}
