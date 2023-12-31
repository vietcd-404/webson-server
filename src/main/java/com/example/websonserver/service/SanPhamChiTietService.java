package com.example.websonserver.service;


import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.dto.request.SanPhamChiTietRequestDemo;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.dto.response.SanPhamChiTietRes;
import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.IntConsumer;

public interface SanPhamChiTietService {
     SanPhamChiTiet createOne(SanPhamChiTietRequest request);

    List<SanPhamChiTiet> createList(List<SanPhamChiTietRequest> listRequest);

     SanPhamChiTiet update(SanPhamChiTietRequest request, Long id);

     Page<SanPhamChiTiet> getAll(Pageable pageable);

    List<SanPhamChiTietResponse> getAllCT();

     void delete(Long id);

     SanPhamChiTiet findById(String id);
    SanPhamChiTietResponse findByIdResponse(String id);

    Page<SanPhamChiTietRes> getAllSanPham(Long maSanPham, Long maLoai,
                                          Long maThuongHieu, Long maMau,
                                          int page, int size,
                                          BigDecimal giaThap, BigDecimal giaCao,
                                          String sortBy, String sortDirection);
    SanPhamChiTiet updateStatus(UpdateTrangThai request, Long id);

    List<SanPhamChiTietRes> Top5SanPhamMoiNhat();
    List<SanPhamChiTietResponse> getAllLoc();
    List<SanPhamChiTietResponse> findTop4BanChay();
    SanPhamChiTiet findDuplicate(String tenSanPham,String tenLoai,String tenMau,String tenThuongHieu);
}
