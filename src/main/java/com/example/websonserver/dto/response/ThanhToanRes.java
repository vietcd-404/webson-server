package com.example.websonserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToanRes {
    public Long tongTien;
    public String moTa ="Thanh toán ví VNPAY" ;
    public Long maHoaDon;
    public String url;
}
