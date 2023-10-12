package com.example.websonserver.service;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.NguoiDungRequest;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface NguoiDungService {
    NguoiDung findByUsername(String username);
    Boolean  existByUsername(String username);
    Boolean  existByEmail(String email);
    NguoiDung saveOrUpdate(NguoiDung nguoiDung);

    NguoiDung create(NguoiDungRequest request);
    NguoiDung update(NguoiDungRequest request,Long id);
    Page<NguoiDung> getAll(Pageable pageable);
    void delete(Long id);
}
