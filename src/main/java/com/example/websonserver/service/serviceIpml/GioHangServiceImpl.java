package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.GioHangRequest;
import com.example.websonserver.entity.GioHang;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.MauSac;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.repository.GioHangRepository;
import com.example.websonserver.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GioHangServiceImpl implements GioHangService {
    @Autowired
    private GioHangRepository gioHangRepository;

    @Override
    public GioHang create(GioHangRequest req) {
        GioHang gh = req.map(new GioHang());
        return gioHangRepository.save(gh);
    }

    @Override
    public GioHang update(GioHangRequest req, Long id) {
        Optional<GioHang> optional = gioHangRepository.findById(id);
        return optional.map(o->{
            o.setNguoiDung(NguoiDung.builder().ten(req.getTenNguoiDung()).build());
            o.setTrangThai(req.getTrangThai());
            o.setXoa(req.getXoa());
            return gioHangRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<GioHang> getAll(Pageable pageable) {
        return gioHangRepository.findAllByXoaFalse(pageable);
    }

    @Override
    public void delete(Long id) {
        gioHangRepository.delete(id);
    }

    @Override
    public GioHang findGioHangByMa(String id) {
        Optional<GioHang> gioHangOptional = gioHangRepository.findById(Long.valueOf(id));
        GioHang gioHang = gioHangOptional.orElse(null);
        return gioHang;
    }
}
