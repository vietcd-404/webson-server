package com.example.websonserver.service;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.MatKhauNguoiDungRequest;
import com.example.websonserver.dto.request.NguoiDungRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.dto.response.NguoiDungResponse;
import com.example.websonserver.entity.Loai;


import com.example.websonserver.entity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface NguoiDungService {
    public NguoiDung create(NguoiDungRequest request);
    public NguoiDung update(NguoiDungRequest request,Long id);
    public List<NguoiDungResponse> getAll();
    public void delete(Long id);
    public NguoiDung findById(String id);
    public List<NguoiDung> searchByKeyword(String keyword);
    NguoiDung findByUsername(String username);
    NguoiDung findById(Long id);
    Boolean  existByUsername(String username);
    Boolean  existByEmail(String email);
    NguoiDung saveOrUpdate(NguoiDung nguoiDung);
    NguoiDung updateStatus(UpdateTrangThai trangThai, Long maNguoiDung);
    NguoiDung changePass(MatKhauNguoiDungRequest nguoiDungRequest, Long maNguoiDung);

}


