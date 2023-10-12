package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.KhuyenMaiRequest;
import com.example.websonserver.dto.request.KhuyenMaiRequest;
import com.example.websonserver.entity.KhuyenMai;
import com.example.websonserver.repository.KhuyenMaiRepository;
import com.example.websonserver.service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KhuyenMaiServiceImpl implements KhuyenMaiService {

    private final KhuyenMaiRepository khuyenMaiRepository;

    @Autowired
    public KhuyenMaiServiceImpl(KhuyenMaiRepository khuyenMaiRepository) {
        this.khuyenMaiRepository = khuyenMaiRepository;
    }


    @Override
    public List<KhuyenMai> getAllKhuyenMai() {
        return khuyenMaiRepository.findAllByXoaFalse();
    }

    @Override
    public KhuyenMai getKhuyenMaiById(Long id) {
        return null;
    }

    @Override
    public KhuyenMai saveKhuyenMai(KhuyenMaiRequest khuyenMai) {
        KhuyenMai km = khuyenMai.map(new KhuyenMai());
        return khuyenMaiRepository.save(km);
    }

    @Override
    public int deleteKhuyenMai(Long id) {
        return khuyenMaiRepository.delete(id);
    }

    @Override
    public KhuyenMai update(Long id, KhuyenMaiRequest kmr) {
        Optional<KhuyenMai> optional = khuyenMaiRepository.findByMaKhuyenMai(id);
        return optional.map(o->{
            o.setDieuKien(kmr.getDieuKien());
            o.setGiaTriGiam(kmr.getGiaTriGiam());
            o.setGiamToiDa(kmr.getGiamToiDa());
            o.setKieuGiamGia(kmr.getKieuGiamGia());
            o.setMoTa(kmr.getMoTa());
            o.setSoLuong(kmr.getSoLuong());
            o.setKhuyenMai(kmr.getKhuyenMai());
            o.setThoiGianBatDau(LocalDateTime.parse(kmr.getThoiGianBatDau()));
            o.setThoiGianKetThuc(LocalDateTime.parse(kmr.getThoiGianKetThuc()));
            o.setTrangThai(kmr.getTrangThai());
            o.setXoa(kmr.getXoa());
            return khuyenMaiRepository.save(o);
        }).orElse(null);
    }
}
