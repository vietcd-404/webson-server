package com.example.websonserver.repository;

import com.example.websonserver.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet,Long> {
    @Query("SELECT hdct FROM HoaDonChiTiet hdct WHERE hdct.sanPhamChiTiet.maSanPhamCT = ?1 AND hdct.hoaDon.maHoaDon = ?2")
    HoaDonChiTiet findByMaSPCTAndMaHD(Long maSPCT, Long maHD);

    List<HoaDonChiTiet> findByHoaDon(HoaDon hoaDon);

    HoaDonChiTiet findByHoaDonAndSanPhamChiTiet(HoaDon hoaDon, SanPhamChiTiet sanPhamChiTiet);

}
