package com.example.websonserver.service;

import com.example.websonserver.dto.request.GioHangRequest;
import com.example.websonserver.entity.GioHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GioHangService {
    public GioHang create(GioHangRequest req);
    public GioHang update(GioHangRequest req,Long id);
    public Page<GioHang> getAll(Pageable pageable);
    public void delete(Long id);
    GioHang findGioHangByMa(String id);
    GioHang findByMaNguoiDung(Long maND);
}
