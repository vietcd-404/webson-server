package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.entity.*;
import com.example.websonserver.repository.AnhSanPhamRepository;
import com.example.websonserver.repository.SanPhamChiTietRepository;
import com.example.websonserver.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class SanPhamChiTietServiceImpl implements SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private AnhSanPhamRepository anhSanPhamRepository;

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
        SanPhamChiTiet spct = new SanPhamChiTiet();

        spct.setGiaBan(request.getGiaBan());
        spct.setPhanTramGiam(request.getPhanTramGiam());
        spct.setSoLuongTon(request.getSoLuongTon());
        spct.setSanPham(getSPByTenSP);
        spct.setLoai(loaiService.findByTen(request.getTenLoai()));
        spct.setThuongHieu(thuongHieuService.findByTen(request.getTenThuongHieu()));
        spct.setMauSac(mauSacService.findByTen(request.getTenMau()));
        spct.setTrangThai(request.getTrangThai());
        spct.setXoa(request.getXoa());

        List<AnhSanPham> danhSachAnh = new ArrayList<>();
        List<Long> danhSachAnhIds = request.getDanhSachAnh();
        if (danhSachAnhIds != null && !danhSachAnhIds.isEmpty()) {
         danhSachAnh = anhSanPhamRepository.findAllById(danhSachAnhIds);
        }
        spct.setAnhSanPhamList(danhSachAnh);
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
            List<AnhSanPham> imageUrls = anhSanPhamService.getImagesBySanPhamChiTiet(sanPhamChiTiet.getMaSanPhamCT());
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

    public void addImagesToProductChiTiet(Long productId, List<Long> imageIds) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm chi tiết với mã: " + productId));

        List<AnhSanPham> images = anhSanPhamRepository.findAllById(imageIds);

        // Liên kết các ảnh với sản phẩm chi tiết
        for (AnhSanPham image : images) {
            image.setSanPhamChiTiet(sanPhamChiTiet);
            image.setTrangThai(1);
        }

        // Lưu danh sách ảnh vào cơ sở dữ liệu
        anhSanPhamRepository.saveAll(images);
    }

    public void themNhieuAnhChoSanPham(Long sanPhamId,  List<Long> danhSachIdAnh) {
        SanPhamChiTiet sanPham = sanPhamChiTietRepository.findById(sanPhamId).orElse(null);
        if (sanPham != null) {
            for (Long anhId : danhSachIdAnh) {
                AnhSanPham anh = anhSanPhamRepository.findById(anhId).orElse(null);
                if (anh != null) {
                    sanPham.getAnhSanPhamList().add(anh);
                }
            }
            sanPhamChiTietRepository.save(sanPham);

        } else {

        }
    }

    public SanPhamChiTiet layAnh(Long anhId) {
        return sanPhamChiTietRepository.findById(anhId).orElse(null);
    }
}
