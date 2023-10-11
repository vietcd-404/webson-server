package com.example.websonserver.service;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.SanPhamRequest;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SanPhamService {
    public SanPham create(SanPhamRequest sanPham);
    public SanPham  update(SanPhamRequest sanPham,Long id);
    public Page<SanPham > getAll(Pageable pageable);
    public void delete(Long id);
}
