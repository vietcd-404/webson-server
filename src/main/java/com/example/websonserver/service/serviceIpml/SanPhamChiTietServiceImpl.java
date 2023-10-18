package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.entity.*;
import com.example.websonserver.repository.SanPhamChiTietRepository;
import com.example.websonserver.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class SanPhamChiTietServiceImpl implements SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private AnhSanPhamService anhSanPhamService;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private LoaiService loaiService;

    @Autowired
    private ThuongHieuService thuongHieuService;

    @Autowired
    private MauSacService mauSacService;
    @Override
    public SanPhamChiTiet createOne(SanPhamChiTietRequest request) {
        SanPham getSPByTenSP = sanPhamService.findByTen(request.getTenSanPham());
        SanPhamChiTiet spct = null;
             spct = SanPhamChiTiet.builder()
                    .giaBan(request.getGiaBan())
                    .phanTramGiam(request.getPhanTramGiam())
                    .soLuongTon(request.getSoLuongTon())
                    .sanPham(getSPByTenSP)
                    .loai(loaiService.findByTen(request.getTenLoai()))
                    .thuongHieu(thuongHieuService.findByTen(request.getTenThuongHieu()))
                    .mauSac(mauSacService.findByTen(request.getTenMau()))
//                    .anhSanPhamList(request.getAnhSanPhamList())
                    .trangThai(request.getTrangThai())
                    .xoa(request.getXoa())
                    .build();
        return sanPhamChiTietRepository.save(spct);
    }

    @Override
    public List<SanPhamChiTiet> createList(List<SanPhamChiTietRequest> listRequest) {
        List<SanPhamChiTiet> spctList = new ArrayList<>();
        for (SanPhamChiTietRequest x:listRequest) {
            SanPham getSPByTenSP = sanPhamService.findByTen(x.getTenSanPham());
            SanPhamChiTiet spct = null;
            if (getSPByTenSP !=null){
                spct = SanPhamChiTiet.builder()
                        .giaBan(x.getGiaBan())
                        .phanTramGiam(x.getPhanTramGiam())
                        .soLuongTon(x.getSoLuongTon())
                        .sanPham(getSPByTenSP)
                        .loai(loaiService.findByTen(x.getTenLoai()))
                        .thuongHieu(thuongHieuService.findByTen(x.getTenThuongHieu()))
                        .mauSac(mauSacService.findByTen(x.getTenMau()))
//                    .anhSanPhamList(request.getAnhSanPhamList())
                        .trangThai(x.getTrangThai())
                        .xoa(x.getXoa())
                        .build();
            }
            spctList.add(spct);
        }
        return sanPhamChiTietRepository.saveAll(spctList);
    }


    @Override
    public SanPhamChiTiet update(SanPhamChiTietRequest request, Long id) {
        Optional<SanPhamChiTiet> optional = sanPhamChiTietRepository.findById(id);
        return optional.map(o->{
            o.setGiaBan(request.getGiaBan());
            o.setPhanTramGiam(request.getPhanTramGiam());
            o.setSoLuongTon((request.getSoLuongTon()));
            o.setSanPham(sanPhamService.findByTen(request.getTenSanPham()));
            o.setLoai(loaiService.findByTen(request.getTenLoai()));
            o.setThuongHieu(thuongHieuService.findByTen(request.getTenThuongHieu()));
            o.setMauSac(mauSacService.findByTen(request.getTenMau()));
//            o.setAnhSanPhamList(request.getAnhSanPhamList());
            o.setTrangThai(request.getTrangThai());
            o.setXoa(request.getXoa());
            return sanPhamChiTietRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<SanPhamChiTiet> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<SanPhamChiTietResponse> getAllCT() {
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietRepository.findAllByXoaFalse();
        List<SanPhamChiTietResponse> sanPhamChiTietDtos = new ArrayList<>();
        for (SanPhamChiTiet sanPhamChiTiet : sanPhamChiTietList) {
            SanPhamChiTietResponse dto = new SanPhamChiTietResponse();
            dto.setMaSanPhamCT(sanPhamChiTiet.getMaSanPhamCT());
            dto.setGiaBan(sanPhamChiTiet.getGiaBan());
            dto.setPhanTramGiam(sanPhamChiTiet.getPhanTramGiam());
            dto.setSoLuongTon(sanPhamChiTiet.getSoLuongTon());
            SanPham sanPham = sanPhamChiTiet.getSanPham();
            if (sanPham != null) {
                String tenSanPham = sanPham.getTenSanPham();
                dto.setTenSanPham(tenSanPham);
            }
            Loai loai = sanPhamChiTiet.getLoai();
            if (loai != null) {
                String tenLoai = loai.getTenLoai();
                dto.setTenLoai(tenLoai);
            }

            ThuongHieu thuongHieu = sanPhamChiTiet.getThuongHieu();
            if (thuongHieu != null) {
                String tenthuongHieu = thuongHieu.getTenThuongHieu();
                dto.setTenThuongHieu(tenthuongHieu);
            }
            MauSac mauSac = sanPhamChiTiet.getMauSac();
            if (mauSac != null) {
                String tenMau = mauSac.getTenMau();
                dto.setTenMau(tenMau);
            }
            List<String> imageUrls = anhSanPhamService.getImagesBySanPhamChiTiet(sanPhamChiTiet.getMaSanPhamCT());
            dto.setDanhSachAnh(imageUrls);
            dto.setTrangThai(sanPhamChiTiet.getTrangThai());
            sanPhamChiTietDtos.add(dto);
        }

        return sanPhamChiTietDtos;
    }

    @Override
    public void delete(Long id) {
        sanPhamChiTietRepository.delete(id);
    }

    @Override
    public SanPhamChiTiet findById(String id) {
        Optional<SanPhamChiTiet>anhsp = sanPhamChiTietRepository.findById(Long.parseLong(id));
        SanPhamChiTiet spct = anhsp.orElse(null);
        return spct;
    }
}
