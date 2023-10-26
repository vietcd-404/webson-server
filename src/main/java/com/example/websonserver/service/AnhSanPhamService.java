package com.example.websonserver.service;

import com.example.websonserver.dto.request.AnhSanPhamRequest;
import com.example.websonserver.entity.AnhSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnhSanPhamService {
    public AnhSanPham create(AnhSanPhamRequest anhSP, MultipartFile data);
    public AnhSanPham update(AnhSanPhamRequest anhSP,Long id);
    public Page<AnhSanPham> getAll(Pageable pageable);
    public void delete(Long id);
    public AnhSanPham findById(String id);
    List<AnhSanPham> getImagesBySanPhamChiTiet(Long maSanPhamCT);
}
