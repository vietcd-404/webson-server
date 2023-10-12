package com.example.websonserver.service;

import com.example.websonserver.dto.request.KhuyenMaiRequest;
import com.example.websonserver.entity.KhuyenMai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KhuyenMaiService {;
    List<KhuyenMai> getAllKhuyenMai();
    KhuyenMai getKhuyenMaiById(Long id);
    KhuyenMai saveKhuyenMai(KhuyenMaiRequest khuyenMai);
    int deleteKhuyenMai(Long id);
    KhuyenMai update(Long id, KhuyenMaiRequest khuyenMaiRequest);
}
