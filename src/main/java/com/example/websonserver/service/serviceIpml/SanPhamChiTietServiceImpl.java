package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.entity.*;
import com.example.websonserver.repository.SanPhamChiTietRepository;
import com.example.websonserver.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class SanPhamChiTietServiceImpl implements SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Override
    public SanPhamChiTiet create(SanPhamChiTietRequest request) {
        SanPhamChiTiet spct = request.map(new SanPhamChiTiet());
        return sanPhamChiTietRepository.save(spct);
    }

    @Override
    public SanPhamChiTiet update(SanPhamChiTietRequest request, Long id) {
        Optional<SanPhamChiTiet> optional = sanPhamChiTietRepository.findById(id);
        return optional.map(o->{
            o.setGiaBan(request.getGiaBan());
            o.setGiaNhap(request.getGiaNhap());
            o.setSoLuongTon((request.getSoLuongTon()));
            o.setSanPham(SanPham.builder().maSanPham(Long.parseLong(request.getMaSP())).build());
            o.setThuongHieu(ThuongHieu.builder().maThuongHieu(Long.parseLong(request.getMaThuongHieu())).build());
            o.setMauSac(MauSac.builder().maMau(Long.parseLong(request.getMaMau())).build());
            o.setAnhSanPham(AnhSanPham.builder().maAnh(Long.parseLong(request.getMaAnh())).build());
            o.setTrangThai(request.getTrangThai());
            o.setXoa(request.getXoa());
            return sanPhamChiTietRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<SanPhamChiTiet> getAll(Pageable pageable) {
        return sanPhamChiTietRepository.findAllByXoaFalse(pageable);
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
