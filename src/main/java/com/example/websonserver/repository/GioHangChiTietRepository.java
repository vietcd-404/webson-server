package com.example.websonserver.repository;

import com.example.websonserver.entity.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet,Long> {
    @Query("SELECT ghct FROM GioHangChiTiet ghct WHERE ghct.gioHang.maGioHang = ?1 AND ghct.sanPhamChiTiet.maSanPhamCT = ?2")
    GioHangChiTiet findCartItemByMaGHAndMaSPCT(Long gioHangId, Long spctId);
}
