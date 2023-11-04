package com.example.websonserver.service;

import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.dto.response.GioHangChiTietResponse;
import com.example.websonserver.entity.GioHangChiTiet;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GioHangChiTietService {
    GioHangChiTietResponse addProductToCart(String SPCTId, String soLuong);

    List<GioHangChiTietResponse> getAllCarts(Pageable pageable);

    List<GioHangChiTietResponse> getCart(Pageable pageable,HttpSession session);

    GioHangChiTietResponse updateProductQuantityInCart(String SPCTId, String soLuong);

    void updateProductInCarts(SanPhamChiTietRequest request, Long maSPCT);

    void deleteProductFromCart(Long maSPCT);
}
