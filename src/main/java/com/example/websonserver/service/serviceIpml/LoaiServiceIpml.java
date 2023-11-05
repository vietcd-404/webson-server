package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.repository.LoaiRepository;
import com.example.websonserver.service.LoaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import java.util.Optional;

@Service
public class LoaiServiceIpml implements LoaiService {
    @Autowired
    private LoaiRepository loaiRepository;

    @Override
    public Loai create(LoaiResquest loai) {
        Loai loai1 = loai.map(new Loai());
        return loaiRepository.save(loai1);
    }

    @Override
    public Loai update(LoaiResquest loai,Long id) {
        Optional<Loai> optional = loaiRepository.findById(id);
        return optional.map(o->{
            o.setTenLoai(loai.getTenLoai());
            return loaiRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<Loai> getAll(Pageable pageable) {
        return loaiRepository.findAllByXoaFalseOrderByMaLoaiDesc(pageable);
    }

    @Override
    @Transient
    public void delete(Long id) {
         loaiRepository.delete(id);
    }

    @Override
    public Loai findByMa(String ma) {
        Optional<Loai> loaiOptional =loaiRepository.findById(Long.valueOf(ma));
        Loai loai = loaiOptional.orElse(null);
        return loai;
    }

    @Override
    public Loai findByTen(String ten) {
        return loaiRepository.findByTen(ten);
    }
}
