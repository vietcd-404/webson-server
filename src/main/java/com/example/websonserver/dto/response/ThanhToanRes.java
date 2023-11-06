package com.example.websonserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToanRes {
    public Long tongTien;
    public String moTa ;
    public Long maHoaDon;
}
