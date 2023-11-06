package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.ThuongHieuRequest;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.ThuongHieu;
import com.example.websonserver.repository.LoaiRepository;
import com.example.websonserver.repository.ThuongHieuRepository;
import com.example.websonserver.service.LoaiService;
import com.example.websonserver.service.ThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ThuongHieuServiceImpl implements ThuongHieuService {
    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    @Override
    public ThuongHieu create(ThuongHieuRequest thuongHieu) {
        ThuongHieu thuongHieu1 = thuongHieu.map(new ThuongHieu());
        return thuongHieuRepository.save(thuongHieu1);
    }

    @Override
    public ThuongHieu update(ThuongHieuRequest thuongHieu,Long id) {
        Optional<ThuongHieu> optional = thuongHieuRepository.findById(id);
        return optional.map(o->{
            o.setTenThuongHieu(thuongHieu.getTenThuongHieu());
            return thuongHieuRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<ThuongHieu> getAll(Pageable pageable) {
        return thuongHieuRepository.findAllByXoaFalse(pageable);
    }

    @Override
    @Transient
    public void delete(Long id) {
        thuongHieuRepository.delete(id);
    }

    @Override
    public ThuongHieu findByMa(String ma) {
        Optional<ThuongHieu> thuongHieuOptional =thuongHieuRepository.findById(Long.valueOf(ma));
        ThuongHieu thuongHieu = thuongHieuOptional.orElse(null);
        return thuongHieu;
    }

    @Override
    public ThuongHieu findByTen(String ten) {
        return thuongHieuRepository.findByTen(ten);
    }
}