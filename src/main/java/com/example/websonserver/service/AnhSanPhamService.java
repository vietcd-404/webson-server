package com.example.websonserver.service;

import com.example.websonserver.dto.request.AnhSanPhamRequest;
import com.example.websonserver.entity.AnhSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnhSanPhamService {
     AnhSanPham create(AnhSanPhamRequest anhSP, MultipartFile data);
     AnhSanPham update(AnhSanPhamRequest anhSP,Long id);
     Page<AnhSanPham> getAll(Pageable pageable);
     void delete(Long id);
     AnhSanPham findById(String id);
    List<String> getImagesBySanPhamChiTiet(Long maSanPhamCT);
}
