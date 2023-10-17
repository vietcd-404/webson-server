package com.example.websonserver.service;


import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SanPhamChiTietService {
    public SanPhamChiTiet create(SanPhamChiTietRequest request);
    public SanPhamChiTiet update(SanPhamChiTietRequest request,Long id);
    public Page<SanPhamChiTiet> getAll(Pageable pageable);
    List<SanPhamChiTietResponse> getAllCT();
    public void delete(Long id);
    public SanPhamChiTiet findById(String id);
}
