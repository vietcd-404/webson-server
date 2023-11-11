package com.example.websonserver.service;

import com.example.websonserver.dto.response.GioHangChiTietResponse;
import com.example.websonserver.entity.SanPhamChiTiet;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GioHangCTSessionService {
    @Autowired
    SanPhamChiTietService sanPhamChiTietService;
    public void addToSessionCart(String SPCTId, int quantity, HttpSession session) {
        // Lấy giỏ hàng từ session
        Map<String, Integer> sessionCart = (Map<String, Integer>) session.getAttribute("sessionCart");

        if (sessionCart == null) {
            sessionCart = new HashMap<>();
        }

        // Kiểm tra xem sản phẩm có trong giỏ hàng session hay không
        if (sessionCart.containsKey(SPCTId)) {
            int currentQuantity = sessionCart.get(SPCTId);
            int newQuantity = currentQuantity + quantity;
            SanPhamChiTiet spctCheckWithMaSPCTSession = sanPhamChiTietService.findById(SPCTId);
            if (currentQuantity > 0 && newQuantity <= spctCheckWithMaSPCTSession.getSoLuongTon()){
                sessionCart.put(SPCTId, newQuantity);
            } else {
                String errorMessage = "Số lượng không hợp lệ.";
                throw new RuntimeException(errorMessage);
            }
        } else {
            // Nếu sản phẩm chưa tồn tại trong giỏ hàng, thêm mới
            SanPhamChiTiet spctCheckWithMaSPCTSession = sanPhamChiTietService.findById(SPCTId);
            if (quantity > 0 && quantity <= spctCheckWithMaSPCTSession.getSoLuongTon()){
                sessionCart.put(SPCTId, quantity);
            } else {
                String errorMessage = "Số lượng không hợp lệ.";
                throw new RuntimeException(errorMessage);
            }
        }

        // Lưu lại giỏ hàng vào session
        session.setAttribute("sessionCart", sessionCart);
    }
    public void updateQuantityInSessionCart(HttpSession session, String SPCTId, int newQuantity) {
        Map<String, Integer> sessionCart = (Map<String, Integer>) session.getAttribute("sessionCart");

        if (sessionCart != null && sessionCart.containsKey(SPCTId)) {
            SanPhamChiTiet spctCheckWithMaSPCTSession = sanPhamChiTietService.findById(SPCTId);
            if (newQuantity > 0 && newQuantity <= spctCheckWithMaSPCTSession.getSoLuongTon()){
                sessionCart.put(SPCTId, newQuantity);
                session.setAttribute("sessionCart", sessionCart);
            } else {
                String errorMessage = "Số lượng không hợp lệ.";
                throw new RuntimeException(errorMessage);
            }
        }
    }
    public void updateProductInSessionCart(HttpSession session, String oldSPCTId, String newSPCTId, int newQuantity) {
        Map<String, Integer> sessionCart = (Map<String, Integer>) session.getAttribute("sessionCart");

        if (sessionCart != null && sessionCart.containsKey(oldSPCTId)) {
            int quantity = sessionCart.get(oldSPCTId);
            sessionCart.remove(oldSPCTId);
            sessionCart.put(newSPCTId, newQuantity);
            session.setAttribute("sessionCart", sessionCart);
        }
    }

    public void removeFromSessionCart(String SPCTId, HttpSession session) {
        // Lấy giỏ hàng từ session
        Map<String, Integer> sessionCart = (Map<String, Integer>) session.getAttribute("sessionCart");

        if (sessionCart != null) {
            // Xóa sản phẩm khỏi giỏ hàng session
            sessionCart.remove(SPCTId);
            // Lưu lại giỏ hàng vào session
            session.setAttribute("sessionCart", sessionCart);
        }
    }

    public void clearSessionCart(HttpSession session) {
        // Xóa toàn bộ giỏ hàng session
        session.removeAttribute("sessionCart");
    }

    public Map<String, Integer> getSessionCart(HttpSession session) {
        // Lấy giỏ hàng từ session
        return (Map<String, Integer>) session.getAttribute("sessionCart");
    }

    public List<GioHangChiTietResponse> getSessionCartDTO (HttpSession session){
        Map<String, Integer> map = (Map<String, Integer>) session.getAttribute("sessionCart");
        List<GioHangChiTietResponse> lstGHSession = new ArrayList<>();
        for (String x : map.keySet()){
            SanPhamChiTiet spct = sanPhamChiTietService.findById(x);
            GioHangChiTietResponse ghct = GioHangChiTietResponse.builder()
                    .soLuong(map.get(x))
                    .tenThuongHieu(spct.getThuongHieu().getTenThuongHieu())
                    .tenSanPham(spct.getSanPham().getTenSanPham())
                    .tenMauSac(spct.getMauSac().getTenMau())
                    .phanTramGiam(spct.getPhanTramGiam())
                    .donGia(spct.getGiaBan())
                    .tenLoai(spct.getLoai().getTenLoai())
                    .build();
            lstGHSession.add(ghct);
        }
        return lstGHSession;
    }
}
