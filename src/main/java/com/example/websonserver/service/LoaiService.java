package com.example.websonserver.service;


import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.entity.Loai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoaiService {
    public Loai create(LoaiResquest loai);
    public Loai update(LoaiResquest loai,Long id);
    public Page<Loai> getAll(Pageable pageable);
    public void delete(Long id);
    public Loai findByMa(String ma);
    public Loai findByTen(String ten);
    public Loai updateStatusLoai(UpdateTrangThai trangThai, Long id);
    List<Loai> fillComboSpct();
}
