package com.example.websonserver.service;

import com.example.websonserver.dto.request.MauSacRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MauSacService {
     MauSac create(MauSacRequest mauSac);
     MauSac update(MauSacRequest mauSac,Long id);
     Page<MauSac> getAll(Pageable pageable);
     void delete(Long id);
     MauSac findByMa(String ma);
     MauSac findByTen(String ten);
     MauSac updateStatus(UpdateTrangThai trangThai, Long id);
    List<MauSac> fillComboSpctByNMau();

}
