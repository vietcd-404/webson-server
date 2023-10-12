package com.example.websonserver.service;


import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.entity.Loai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoaiService {
    public Loai create(LoaiResquest loai);
    public Loai update(LoaiResquest loai,Long id);
    public Page<Loai> getAll(Pageable pageable);
    public void delete(Long id);
}
