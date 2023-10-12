package com.example.websonserver.service;

import com.example.websonserver.dto.request.DiaChiRequest;
import com.example.websonserver.entity.DiaChi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiaChiService {
    public DiaChi create(DiaChiRequest request);
    public DiaChi update(DiaChiRequest request,Long id);
    public Page<DiaChi> getAll(Pageable pageable);
    public void delete(Long id);
    public DiaChi findById(String id);
}
