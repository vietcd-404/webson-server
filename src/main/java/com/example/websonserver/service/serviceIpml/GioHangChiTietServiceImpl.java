package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.dto.response.GioHangChiTietResponse;
import com.example.websonserver.entity.GioHang;
import com.example.websonserver.entity.GioHangChiTiet;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.entity.SanPhamChiTiet;
import com.example.websonserver.repository.GioHangChiTietRepository;
import com.example.websonserver.repository.GioHangRepository;
import com.example.websonserver.repository.NguoiDungRepository;
import com.example.websonserver.repository.SanPhamChiTietRepository;
import com.example.websonserver.service.GioHangCTSessionService;
import com.example.websonserver.service.GioHangChiTietService;
import com.example.websonserver.service.GioHangService;
import com.example.websonserver.service.SanPhamChiTietService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Autowired
    GioHangCTSessionService gioHangCTSessionService;

    @Override
    public GioHangChiTietResponse addProductToCart(String SPCTId, String soLuong) {
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
        GioHangChiTietResponse gioHangChiTietResponse = GioHangChiTietResponse.builder()
                .tenLoai(ghctWithIdSPCT.getSanPhamChiTiet().getLoai().getTenLoai())
                .donGia(ghctWithIdSPCT.getSanPhamChiTiet().getGiaBan())
                .phanTramGiam(ghctWithIdSPCT.getSanPhamChiTiet().getPhanTramGiam())
                .tenMauSac(ghctWithIdSPCT.getSanPhamChiTiet().getMauSac().getTenMau())
                .tenSanPham(ghctWithIdSPCT.getSanPhamChiTiet().getSanPham().getTenSanPham())
                .tenThuongHieu(ghctWithIdSPCT.getSanPhamChiTiet().getThuongHieu().getTenThuongHieu())
                .soLuong(ghctWithIdSPCT.getSoLuong())
                .build();
        return gioHangChiTietResponse;
    }

    @Override
    public List<GioHangChiTietResponse> getAllCarts(Pageable pageable) {
        Page<GioHangChiTiet> gh =  gioHangChiTietRepository.findAllByXoaFalse(pageable);
        List<GioHangChiTietResponse> listGHCTResponse = new ArrayList<>();
        for (GioHangChiTiet item : gh.getContent()){
            GioHangChiTietResponse ghForm = GioHangChiTietResponse.builder()
                    .tenLoai(item.getSanPhamChiTiet().getLoai().getTenLoai())
                    .donGia(item.getSanPhamChiTiet().getGiaBan())
                    .phanTramGiam(item.getSanPhamChiTiet().getPhanTramGiam())
                    .tenMauSac(item.getSanPhamChiTiet().getMauSac().getTenMau())
                    .tenSanPham(item.getSanPhamChiTiet().getSanPham().getTenSanPham())
                    .tenThuongHieu(item.getSanPhamChiTiet().getThuongHieu().getTenThuongHieu())
                    .soLuong(item.getSoLuong())
                    .build();
            listGHCTResponse.add(ghForm);
        }
        return  listGHCTResponse;
    }

    @Override
    public List<GioHangChiTietResponse> getCart(Pageable pageable,HttpSession session) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        NguoiDung nguoiDung = nguoiDungRepository.findByUsername(username);
        GioHang gioHang = gioHangService.findByMaNguoiDung(nguoiDung.getMaNguoiDung());
        if (gioHang == null) {
            gioHang = new GioHang();
            gioHang.setNguoiDung(nguoiDung);
            gioHang.setTrangThai(0);
            gioHang.setXoa(Boolean.FALSE);
            gioHangRepository.save(gioHang);
        }
        Map<String, Integer> ghctSession = gioHangCTSessionService.getSessionCart(session);
        if (ghctSession != null) {
            for (String maspct : ghctSession.keySet()) {
                GioHangChiTiet ghctWithSession = gioHangChiTietRepository.findCartItemByMaGHAndMaSPCT(gioHang.getMaGioHang(), Long.parseLong(maspct));
                int quantitySession = ghctSession.get(maspct);
                SanPhamChiTiet spctCheckWithMaSPCTSession = sanPhamChiTietService.findById(maspct);
                if (ghctWithSession != null) {
                    if (quantitySession > 0 && quantitySession <= spctCheckWithMaSPCTSession.getSoLuongTon()) {
                        ghctWithSession.setSoLuong(ghctWithSession.getSoLuong() + quantitySession);
                        spctCheckWithMaSPCTSession.setSoLuongTon(spctCheckWithMaSPCTSession.getSoLuongTon() - quantitySession);
                        gioHangCTSessionService.removeFromSessionCart(maspct,session);
                        // Cập nhật thông tin vào cơ sở dữ liệu
                        gioHangChiTietRepository.save(ghctWithSession);
                        sanPhamChiTietRepository.save(spctCheckWithMaSPCTSession);
                    } else {
                        String errorMessage = "Số lượng không hợp lệ.";
                        throw new RuntimeException(errorMessage);
                    }
                } else {
                    GioHangChiTiet newGHCT = GioHangChiTiet.builder()
                            .soLuong(quantitySession)
                            .donGia(spctCheckWithMaSPCTSession.getGiaBan())
                            .gioHang(gioHang)
                            .sanPhamChiTiet(spctCheckWithMaSPCTSession)
                            .trangThai(0)
                            .xoa(Boolean.FALSE)
                            .build();
                    gioHangChiTietRepository.save(newGHCT);
                    gioHangCTSessionService.removeFromSessionCart(maspct,session);
                    // Đảm bảo cập nhật số lượng tồn kho
                    spctCheckWithMaSPCTSession.setSoLuongTon(spctCheckWithMaSPCTSession.getSoLuongTon() - quantitySession);
                    sanPhamChiTietRepository.save(spctCheckWithMaSPCTSession);
                }
            }
        }
        Page<GioHangChiTiet> gh =  gioHangChiTietRepository.findByMaGioHang(gioHang.getMaGioHang(),pageable);
        List<GioHangChiTietResponse> listGHCTResponse = new ArrayList<>();
        for (GioHangChiTiet item : gh.getContent()){
            GioHangChiTietResponse ghForm = GioHangChiTietResponse.builder()
                    .tenLoai(item.getSanPhamChiTiet().getLoai().getTenLoai())
                    .donGia(item.getSanPhamChiTiet().getGiaBan())
                    .phanTramGiam(item.getSanPhamChiTiet().getPhanTramGiam())
                    .tenMauSac(item.getSanPhamChiTiet().getMauSac().getTenMau())
                    .tenSanPham(item.getSanPhamChiTiet().getSanPham().getTenSanPham())
                    .tenThuongHieu(item.getSanPhamChiTiet().getThuongHieu().getTenThuongHieu())
                    .soLuong(item.getSoLuong())
                    .build();
            listGHCTResponse.add(ghForm);
        }
        return  listGHCTResponse;
    }

    @Override
    public GioHangChiTietResponse updateProductQuantityInCart(String SPCTId, String soLuong) {
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
        GioHangChiTietResponse gioHangChiTietResponse = GioHangChiTietResponse.builder()
                .tenLoai(ghctWithIdSPCT.getSanPhamChiTiet().getLoai().getTenLoai())
                .donGia(ghctWithIdSPCT.getSanPhamChiTiet().getGiaBan())
                .phanTramGiam(ghctWithIdSPCT.getSanPhamChiTiet().getPhanTramGiam())
                .tenMauSac(ghctWithIdSPCT.getSanPhamChiTiet().getMauSac().getTenMau())
                .tenSanPham(ghctWithIdSPCT.getSanPhamChiTiet().getSanPham().getTenSanPham())
                .tenThuongHieu(ghctWithIdSPCT.getSanPhamChiTiet().getThuongHieu().getTenThuongHieu())
                .soLuong(ghctWithIdSPCT.getSoLuong())
                .build();
        return gioHangChiTietResponse;
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
        GioHangChiTiet ghctWithIdSPCT = gioHangChiTietRepository.findCartItemByMaGHAndMaSPCT(gioHang.getMaGioHang(),maSPCT);
        SanPhamChiTiet spct = sanPhamChiTietService.findById(String.valueOf(maSPCT));
        spct.setSoLuongTon(spct.getSoLuongTon()+ ghctWithIdSPCT.getSoLuong());
        sanPhamChiTietRepository.save(spct);
        gioHangChiTietRepository.delete(gioHang.getMaGioHang(),maSPCT);
    }
}
