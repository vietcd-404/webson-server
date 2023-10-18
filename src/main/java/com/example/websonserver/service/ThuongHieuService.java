package com.example.websonserver.service;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.ThuongHieuRequest;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.MauSac;
import com.example.websonserver.entity.SanPham;
import com.example.websonserver.entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ThuongHieuService {
    public ThuongHieu create(ThuongHieuRequest thuongHieu);
    public ThuongHieu update(ThuongHieuRequest thuongHieu,Long id);
    public Page<ThuongHieu> getAll(Pageable pageable);
    public void delete(Long id);
    public ThuongHieu findByMa(String ma);
    public ThuongHieu findByTen(String ten);
}
