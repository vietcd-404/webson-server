package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.response.DanhSachYTResponse;
import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.entity.DanhSachYeuThich;
import com.example.websonserver.entity.GioHangChiTiet;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.entity.SanPhamChiTiet;
import com.example.websonserver.repository.DanhSachYeuThichRepository;
import com.example.websonserver.service.DanhSachYeuThichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DanhSachYeuThichServiceImpl implements DanhSachYeuThichService {

    @Autowired
    SanPhamChiTietServiceImpl sanPhamChiTietService;

    @Autowired
    NguoiDungServiceImpl nguoiDungService;

    @Autowired
    DanhSachYeuThichRepository repository;



    @Override
    public DanhSachYeuThich addProduct(Long maSPCT) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        NguoiDung nguoiDung = nguoiDungService.findByUsername(username);
        SanPhamChiTiet sanPhamChiTietRequest = sanPhamChiTietService.findById(String.valueOf(maSPCT));
        DanhSachYeuThich newDs = DanhSachYeuThich.builder()
        .sanPhamChiTiet(sanPhamChiTietRequest)
                .nguoiDung(nguoiDung)
                .build();
        return repository.save(newDs);
    }

    @Override
    public List<DanhSachYTResponse> getAllList(Pageable pageable) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        NguoiDung nguoiDung = nguoiDungService.findByUsername(username);
        Page<DanhSachYeuThich> chiTiets = repository.findByMaNguoiDung(nguoiDung.getMaNguoiDung(),pageable);
        List<DanhSachYTResponse> danhSachYTResponses = new ArrayList<>();
        for (DanhSachYeuThich item: chiTiets.getContent()
             ) {
            DanhSachYTResponse danhSachYTResponse = DanhSachYTResponse.builder()
                    .maSanPhamCT(item.getSanPhamChiTiet().getMaSanPhamCT())
                    .tenLoai(item.getSanPhamChiTiet().getLoai().getTenLoai())
                    .donGia(item.getSanPhamChiTiet().getGiaBan())
                    .phanTramGiam(item.getSanPhamChiTiet().getPhanTramGiam())
                    .tenMauSac(item.getSanPhamChiTiet().getMauSac().getTenMau())
                    .tenSanPham(item.getSanPhamChiTiet().getSanPham().getTenSanPham())
                    .tenThuongHieu(item.getSanPhamChiTiet().getThuongHieu().getTenThuongHieu())
                    .anh(item.getSanPhamChiTiet().getAnhSanPhamList().get(0).getAnh())
                    .build();
            danhSachYTResponses.add(danhSachYTResponse);
        }
        return danhSachYTResponses;
    }

    @Override
    public DanhSachYeuThich findByUser(Long maSPCT) {
        UserDetails  userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        NguoiDung nguoiDung = nguoiDungService.findByUsername(username);
        DanhSachYeuThich danhSachYeuThich = repository.findByUserAndProduct(nguoiDung.getMaNguoiDung(),maSPCT);
        return danhSachYeuThich;
    }

    @Override
    public void deleteProductFromlist(Long maSPCT) {
        UserDetails  userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        NguoiDung nguoiDung = nguoiDungService.findByUsername(username);
        DanhSachYeuThich danhSachYeuThich = repository.findByUserAndProduct(nguoiDung.getMaNguoiDung(),maSPCT);
        repository.delete(danhSachYeuThich);
    }

    @Override
    public List<SanPhamChiTietResponse> thongKeTopSanPhamYeuThich() {
        Pageable top3Pageable = PageRequest.of(0, 4);
        List<Object[]> top3Objects = repository.thongKeTopSanPhamYeuThich(top3Pageable);
        List<SanPhamChiTietResponse> ds12= new ArrayList<>();
        for (Object[] obj: top3Objects) {
            Long maSanPham = (Long) obj[0];
            Long soLg = (Long) obj[1];
            Integer soLuong = soLg.intValue();
            SanPhamChiTiet spct = sanPhamChiTietService.findById(String.valueOf(maSanPham));
            if (spct != null) {
                SanPhamChiTietResponse sanPhamChiTietResponse = new SanPhamChiTietResponse();
                sanPhamChiTietResponse.setTenSanPham(spct.getSanPham().getTenSanPham()+" "+spct.getMauSac().getTenMau());
                sanPhamChiTietResponse.setSoLuongTon(soLuong);
                sanPhamChiTietResponse.setGiaBan(spct.getGiaBan());
                ds12.add(sanPhamChiTietResponse);
            }
        }
        return ds12;
    }
}
