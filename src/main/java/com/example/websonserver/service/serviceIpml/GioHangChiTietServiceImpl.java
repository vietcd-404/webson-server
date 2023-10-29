package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.entity.GioHang;
import com.example.websonserver.entity.GioHangChiTiet;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.entity.SanPhamChiTiet;
import com.example.websonserver.repository.GioHangChiTietRepository;
import com.example.websonserver.repository.GioHangRepository;
import com.example.websonserver.repository.NguoiDungRepository;
import com.example.websonserver.repository.SanPhamChiTietRepository;
import com.example.websonserver.service.GioHangChiTietService;
import com.example.websonserver.service.GioHangService;
import com.example.websonserver.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    NguoiDungRepository nguoiDungRepository;
    @Autowired
    GioHangRepository gioHangRepository;

    @Override
    public GioHangChiTiet addProductToCart(String SPCTId, String soLuong) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        NguoiDung nguoiDung = nguoiDungRepository.findByUsername(username);
        int quantity = Integer.parseInt(soLuong);
        GioHang gioHang = gioHangService.findByMaNguoiDung(nguoiDung.getMaNguoiDung());
        if (gioHang == null){
            gioHang = new GioHang();
            gioHang.setNguoiDung(nguoiDung);
            gioHang.setTrangThai(0);
            gioHang.setXoa(Boolean.FALSE);
            gioHangRepository.save(gioHang);
        }
        SanPhamChiTiet spct = sanPhamChiTietService.findById(SPCTId);
        GioHangChiTiet ghctWithIdSPCT = null;
        try{
            ghctWithIdSPCT = gioHangChiTietRepository.findCartItemByMaGHAndMaSPCT(gioHang.getMaGioHang(),Long.parseLong(SPCTId));
        }catch (Exception e){
            e.printStackTrace();
        }
        if (spct == null){
            String errorMessage = "Không tìm thấy sản phẩm chi tiết.";
            throw new RuntimeException(errorMessage);
        }
        if (ghctWithIdSPCT != null){
            if (quantity > 0 && quantity<= spct.getSoLuongTon()){
                ghctWithIdSPCT.setSoLuong(ghctWithIdSPCT.getSoLuong()+quantity);
                spct.setSoLuongTon(spct.getSoLuongTon()-quantity);

                // Cập nhật thông tin vào cơ sở dữ liệu
                gioHangChiTietRepository.save(ghctWithIdSPCT);
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
                    .trangThai(0)
                    .xoa(Boolean.FALSE)
                    .build();
            gioHangChiTietRepository.save(newGHCT);
            // Đảm bảo cập nhật số lượng tồn kho
            spct.setSoLuongTon(spct.getSoLuongTon() - quantity);
            sanPhamChiTietRepository.save(spct);
            ghctWithIdSPCT = newGHCT;
        }
        return ghctWithIdSPCT;
    }

    @Override
    public Page<GioHangChiTiet> getAllCarts(Pageable pageable) {
        return gioHangChiTietRepository.findAllByXoaFalse(pageable);
    }

    @Override
    public GioHangChiTiet getCart(String emailId, Long cartId) {
        return null;
    }

    @Override
    public GioHangChiTiet updateProductQuantityInCart(String SPCTId, String soLuong) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        NguoiDung nguoiDung = nguoiDungRepository.findByUsername(username);
        int quantity = Integer.parseInt(soLuong);
        GioHang gioHang = gioHangService.findByMaNguoiDung(nguoiDung.getMaNguoiDung());
        SanPhamChiTiet spct = sanPhamChiTietService.findById(SPCTId);
        GioHangChiTiet ghctWithIdSPCT = null;
        try{
            ghctWithIdSPCT = gioHangChiTietRepository.findCartItemByMaGHAndMaSPCT(gioHang.getMaGioHang(),Long.parseLong(SPCTId));
        }catch (Exception e){
            e.printStackTrace();
        }
        if (spct == null){
            String errorMessage = "Không tìm thấy sản phẩm chi tiết.";
            throw new RuntimeException(errorMessage);
        }
        if (ghctWithIdSPCT != null){
            spct.setSoLuongTon(spct.getSoLuongTon()+ ghctWithIdSPCT.getSoLuong());
            // Cập nhật thông tin vào cơ sở dữ liệu
            sanPhamChiTietRepository.save(spct);
            if (quantity > 0 && quantity<= spct.getSoLuongTon()){
                ghctWithIdSPCT.setSoLuong(quantity);
                spct.setSoLuongTon(spct.getSoLuongTon()-quantity);
                // Cập nhật thông tin vào cơ sở dữ liệu
                gioHangChiTietRepository.save(ghctWithIdSPCT);
                sanPhamChiTietRepository.save(spct);
            }else {
                String errorMessage = "Số lượng không hợp lệ.";
                throw new RuntimeException(errorMessage);
            }
        }else {
            String errorMessage = "Không tìm thấy giỏ hàng trong giỏ hàng.";
            throw new RuntimeException(errorMessage);
        }
        return ghctWithIdSPCT;
    }

    @Override
    public void updateProductInCarts(SanPhamChiTietRequest request, Long maSPCT) {
        sanPhamChiTietService.update(request,maSPCT);
    }

    @Override
    public void deleteProductFromCart(Long maSPCT) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        NguoiDung nguoiDung = nguoiDungRepository.findByUsername(username);
        GioHang gioHang = gioHangService.findByMaNguoiDung(nguoiDung.getMaNguoiDung());
        gioHangChiTietRepository.delete(gioHang.getMaGioHang(),maSPCT);
    }
}
