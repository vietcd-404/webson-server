package com.example.websonserver.service;

import com.example.websonserver.dto.request.MauSacRequest;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MauSacService {
    public MauSac create(MauSacRequest mauSac);
    public MauSac update(MauSacRequest mauSac,Long id);
    public Page<MauSac> getAll(Pageable pageable);
    public void delete(Long id);
    public MauSac findByMa(String ma);
    public MauSac findByTen(String ten);
}
