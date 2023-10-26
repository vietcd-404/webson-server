package com.example.websonserver.service;

import com.example.websonserver.dto.response.GioHangChiTietResponse;
import com.example.websonserver.entity.GioHangChiTiet;

import java.util.List;

public interface GioHangChiTietService {
    GioHangChiTiet addProductToCart(String gioHangId, String SPCTId, String soLuong);

    List<GioHangChiTiet> getAllCarts();

    GioHangChiTiet getCart(String emailId, Long cartId);

    GioHangChiTiet updateProductQuantityInCart(String gioHangId, String SPCTId, String soLuong);

    void updateProductInCarts(Long cartId, Long productId);

    String deleteProductFromCart(Long cartId, Long productId);
}
