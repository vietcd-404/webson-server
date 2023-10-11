package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.MauSacRequest;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.MauSac;
import com.example.websonserver.repository.LoaiRepository;
import com.example.websonserver.repository.MauSacRepository;
import com.example.websonserver.service.LoaiService;
import com.example.websonserver.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MauSacServiceImpl implements MauSacService {
    @Autowired
    private MauSacRepository mauSacRepository;

    @Override
    public MauSac create(MauSacRequest mauSac) {
        MauSac mauSac1 = mauSac.map(new MauSac());
        return mauSacRepository.save(mauSac1);
    }

    @Override
    public MauSac update(MauSacRequest mauSac,Long id) {
        Optional<MauSac> optional = mauSacRepository.findById(id);
        return optional.map(o->{
            o.setTenMau(mauSac.getTenMau());
            return mauSacRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<MauSac> getAll(Pageable pageable) {
        return mauSacRepository.findAllByXoaFalse(pageable);
    }

    @Override
    @Transient
    public void delete(Long id) {
        mauSacRepository.delete(id);
    }
}
