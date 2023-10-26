package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.response.GioHangChiTietResponse;
import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.entity.GioHang;
import com.example.websonserver.entity.GioHangChiTiet;
import com.example.websonserver.entity.SanPhamChiTiet;
import com.example.websonserver.repository.GioHangChiTietRepository;
import com.example.websonserver.repository.SanPhamChiTietRepository;
import com.example.websonserver.service.GioHangChiTietService;
import com.example.websonserver.service.GioHangService;
import com.example.websonserver.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GioHangChiTietServiceImpl implements GioHangChiTietService {
    @Autowired
    GioHangService gioHangService;
    @Autowired
    SanPhamChiTietService sanPhamChiTietService;
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    GioHangChiTietRepository gioHangChiTietRepository;
    @Override
    public GioHangChiTiet addProductToCart(String gioHangId, String SPCTId, String soLuong) {
        int quantity = Integer.parseInt(soLuong);
        GioHang gioHang = gioHangService.findGioHangByMa(gioHangId);
        SanPhamChiTiet spct = sanPhamChiTietService.findById(SPCTId);
        GioHangChiTiet ghct = gioHangChiTietRepository.findCartItemByMaGHAndMaSPCT(Long.parseLong(gioHangId),Long.parseLong(SPCTId));
        if (spct == null){
            String errorMessage = "Không tìm thấy sản phẩm chi tiết.";
            throw new RuntimeException(errorMessage);
        }
        if (ghct != null){
            if (quantity > 0 && quantity<= spct.getSoLuongTon()){
                ghct.setSoLuong(ghct.getSoLuong()+quantity);
                spct.setSoLuongTon(spct.getSoLuongTon()-quantity);

                // Cập nhật thông tin vào cơ sở dữ liệu
                gioHangChiTietRepository.save(ghct);
                sanPhamChiTietRepository.save(spct);
            }else {
                String errorMessage = "Số lượng không hợp lệ.";
                throw new RuntimeException(errorMessage);
            }
        }else {
            GioHangChiTiet newGHCT = GioHangChiTiet.builder()
                    .soLuong(quantity)
                    .donGia(spct.getGiaBan())
                    .gioHang(gioHang)
                    .sanPhamChiTiet(spct)
                    .trangThai(1)
                    .xoa(Boolean.FALSE)
                    .build();
            gioHangChiTietRepository.save(newGHCT);
            gioHangChiTietRepository.save(newGHCT);
            // Đảm bảo cập nhật số lượng tồn kho
            spct.setSoLuongTon(spct.getSoLuongTon() - quantity);
            sanPhamChiTietRepository.save(spct);
            ghct = newGHCT;
        }
        return ghct;
    }

    @Override
    public List<GioHangChiTiet> getAllCarts() {
        List<GioHangChiTiet> listGHCT = gioHangChiTietRepository.findAll();
        if (listGHCT.size() == 0){
            String errorMessage = "Không có giỏ hàng chi tiết nào.";
            throw new RuntimeException(errorMessage);
        }
        return listGHCT;
    }

    @Override
    public GioHangChiTiet getCart(String emailId, Long cartId) {
        return null;
    }

    @Override
    public GioHangChiTiet updateProductQuantityInCart(String gioHangId, String SPCTId, String soLuong) {
        int quantity = Integer.parseInt(soLuong);
        GioHang gioHang = gioHangService.findGioHangByMa(gioHangId);
        SanPhamChiTiet spct = sanPhamChiTietService.findById(SPCTId);
        GioHangChiTiet ghct = gioHangChiTietRepository.findCartItemByMaGHAndMaSPCT(Long.parseLong(gioHangId),Long.parseLong(SPCTId));
        if (spct == null){
            String errorMessage = "Không tìm thấy sản phẩm chi tiết.";
            throw new RuntimeException(errorMessage);
        }
        if (ghct != null){
            spct.setSoLuongTon(spct.getSoLuongTon()+ ghct.getSoLuong());
            // Cập nhật thông tin vào cơ sở dữ liệu
            sanPhamChiTietRepository.save(spct);
            if (quantity > 0 && quantity<= spct.getSoLuongTon()){
                ghct.setSoLuong(quantity);
                spct.setSoLuongTon(spct.getSoLuongTon()-quantity);
                // Cập nhật thông tin vào cơ sở dữ liệu
                gioHangChiTietRepository.save(ghct);
                sanPhamChiTietRepository.save(spct);
            }else {
                String errorMessage = "Số lượng không hợp lệ.";
                throw new RuntimeException(errorMessage);
            }
        }

        return ghct;
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {

    }

    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        return null;
    }
}
