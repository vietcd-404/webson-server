package com.example.websonserver.service;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.ThuongHieuRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.MauSac;
import com.example.websonserver.entity.SanPham;
import com.example.websonserver.entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ThuongHieuService {
     ThuongHieu create(ThuongHieuRequest thuongHieu);
     ThuongHieu update(ThuongHieuRequest thuongHieu,Long id);
     Page<ThuongHieu> getAll(Pageable pageable);
     void delete(Long id);
     ThuongHieu findByMa(String ma);
     ThuongHieu findByTen(String ten);
     ThuongHieu updateStatus(UpdateTrangThai trangThai, Long id);
    List<ThuongHieu> fillComboSpct();
}
