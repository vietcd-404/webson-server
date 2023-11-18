package com.example.websonserver.service;

import com.example.websonserver.dto.request.HoaDonRequest;
import com.example.websonserver.dto.request.NguoiDungSessionRequest;
import com.example.websonserver.dto.request.UpdateHoaDonRequest;
import com.example.websonserver.dto.response.HoaDonResponse;
import com.example.websonserver.entity.HoaDon;
import com.example.websonserver.entity.HoaDonChiTiet;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;
import java.util.List;

public interface HoaDonService {
    HoaDon placeOrder(HoaDonRequest request, Long maGioHang );


    HoaDon statusHoaDon(HoaDonRequest request, Long maHoaDon);

    List<HoaDonResponse> getOrdersByUser(Principal principal, Integer trangThai);

   HoaDon getHoaDonChiTiet(Long maHoaDon);

    HoaDon updateOrder(UpdateHoaDonRequest request , Long maHoaDon);
    HoaDonChiTiet updateQuantity(Long idSPCT, int soLuong);

    String HuyHoaDon(Long maHD);

    HoaDon hoaDonSession(HttpSession session, NguoiDungSessionRequest request);
}
