package com.example.websonserver.service;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.SanPhamRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SanPhamService {
     SanPham create(SanPhamRequest sanPham);
     SanPham  update(SanPhamRequest sanPham,Long id);
     Page<SanPham > getAll(Pageable pageable);
     void delete(Long id);
     SanPham findByTen(String tenSP);
     SanPham  updateStatus(UpdateTrangThai trangThai, Long id);
    List<SanPham> fillComboSpctBySanPham();
}
