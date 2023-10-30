package com.example.websonserver.service;

import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.entity.GioHangChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GioHangChiTietService {
    GioHangChiTiet addProductToCart(String SPCTId, String soLuong);

    Page<GioHangChiTiet> getAllCarts(Pageable pageable);

    GioHangChiTiet getCart(String emailId, Long cartId);

    GioHangChiTiet updateProductQuantityInCart(String SPCTId, String soLuong);

    void updateProductInCarts(SanPhamChiTietRequest request, Long maSPCT);

    void deleteProductFromCart(Long maSPCT);
}
