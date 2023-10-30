package com.example.websonserver.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class GioHangChiTietResponse {
    private Long maGHCT;
    private double tongTien;
    private List<SanPhamReponse> listSanPhamResponse = new ArrayList<>();
}
