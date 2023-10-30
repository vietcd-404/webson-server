package com.example.websonserver.service;


import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.dto.response.SanPhamChiTietRes;
import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.IntConsumer;

public interface SanPhamChiTietService {
    public SanPhamChiTiet createOne(SanPhamChiTietRequest request);

    public List<SanPhamChiTiet> createList(List<SanPhamChiTietRequest> listRequest);

    public SanPhamChiTiet update(SanPhamChiTietRequest request, Long id);

    public Page<SanPhamChiTiet> getAll(Pageable pageable);

    List<SanPhamChiTietResponse> getAllCT();

    public void delete(Long id);

    public SanPhamChiTiet findById(String id);

    Page<SanPhamChiTietRes> getAllSanPham(Long maSanPham, Long maLoai,
                                          Long maThuongHieu, Long maMau,
                                          int page, int size,
                                          BigDecimal giaThap, BigDecimal giaCao,
                                          String sortBy, String sortDirection);

}
