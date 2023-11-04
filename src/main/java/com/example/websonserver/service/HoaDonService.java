package com.example.websonserver.service;

import com.example.websonserver.dto.request.HoaDonRequest;
import com.example.websonserver.dto.response.HoaDonResponse;
import com.example.websonserver.entity.HoaDon;
import jakarta.servlet.http.HttpServletResponse;

import java.security.Principal;
import java.util.List;

public interface HoaDonService {
    HoaDon placeOrder(HoaDonRequest request, Long maGioHang );


    HoaDon statusHoaDon(HoaDonRequest request, Long maHoaDon);

    List<HoaDonResponse> getOrdersByUser(Principal principal, Integer trangThai);

   HoaDon getHoaDonChiTiet(Long maHoaDon);

    HoaDon updateOrder(String username, Long maHoaDon, int trangThai);
}
