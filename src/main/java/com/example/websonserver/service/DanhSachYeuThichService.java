package com.example.websonserver.service;

import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.dto.response.DanhSachYTResponse;
import com.example.websonserver.dto.response.GioHangChiTietResponse;
import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.entity.DanhSachYeuThich;
import com.example.websonserver.entity.SanPhamChiTiet;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DanhSachYeuThichService {
    DanhSachYeuThich addProduct(Long maSPCT);

    List<DanhSachYTResponse> getAllList(Pageable pageable);

    DanhSachYeuThich findByUser(Long maSPCT);

    void deleteProductFromlist(Long maSPCT);

    List<SanPhamChiTietResponse> thongKeTopSanPhamYeuThich();
}
