package com.example.websonserver.service;

import com.example.websonserver.dto.request.NguoiDungRequest;
import com.example.websonserver.entity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NguoiDungService {
    public NguoiDung create(NguoiDungRequest request);
    public NguoiDung update(NguoiDungRequest request,Long id);
    public Page<NguoiDung> getAll(Pageable pageable);
    public void delete(Long id);
    public NguoiDung findById(String id);
    public List<NguoiDung> searchByKeyword(String keyword);
}
