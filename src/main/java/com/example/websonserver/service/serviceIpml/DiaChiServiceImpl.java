package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.DiaChiRequest;
import com.example.websonserver.entity.DiaChi;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.repository.DiaChiRepository;
import com.example.websonserver.service.DiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiaChiServiceImpl implements DiaChiService {
    @Autowired
    DiaChiRepository diaChiRepository;
    @Override
    public DiaChi create(DiaChiRequest request) {
        DiaChi diaChi = request.map(new DiaChi());
        return diaChiRepository.save(diaChi);
    }

    @Override
    public DiaChi update(DiaChiRequest request, Long id) {
        Optional<DiaChi> optional = diaChiRepository.findById(id);
        return optional.map(o->{
            o.setDiaChi(request.getDiaChi());
            o.setLoai_dia_chi(request.getLoaiDiaChi());
            o.setNguoiDung(NguoiDung.builder().maNguoiDung(Long.parseLong(request.getMaNguoiDung())).build());
            o.setTrangThai(request.getTrangThai());
            o.setXoa(request.getXoa());
            return diaChiRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<DiaChi> getAll(Pageable pageable) {
        return diaChiRepository.findAllByXoaFalse(pageable);
    }

    @Override
    public void delete(Long id) {
        diaChiRepository.delete(id);
    }

    @Override
    public DiaChi findById(String id) {
        Optional<DiaChi>dc = diaChiRepository.findById(Long.parseLong(id));
        DiaChi diaChi = dc.orElse(null);
        return diaChi;
    }
}
