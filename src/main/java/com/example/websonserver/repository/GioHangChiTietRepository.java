package com.example.websonserver.repository;

import com.example.websonserver.entity.GioHang;
import com.example.websonserver.entity.GioHangChiTiet;
import com.example.websonserver.entity.Loai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet,Long> {
    Page<GioHangChiTiet> findAllByXoaFalse(Pageable pageable);
    @Query("SELECT ghct FROM GioHangChiTiet ghct WHERE ghct.gioHang.maGioHang = ?1 AND ghct.sanPhamChiTiet.maSanPhamCT = ?2")
    GioHangChiTiet findCartItemByMaGHAndMaSPCT(Long gioHangId, Long spctId);

    @Query("SELECT ghct FROM GioHangChiTiet ghct WHERE ghct.gioHang.maGioHang = ?1")
    Page<GioHangChiTiet> findByMaGioHang(Long ma,Pageable pageable);

    List<GioHangChiTiet> findByGioHang(GioHang gioHang);

    @Transactional
    @Modifying
    @Query("UPDATE GioHangChiTiet a " +
            "SET a.xoa = true WHERE a.gioHang.maGioHang = ?1 AND a.sanPhamChiTiet.maSanPhamCT = ?2")
    void delete(Long maGH ,Long maSPCT);

//    @Transactional
//    @Query("DELETE FROM GioHangChiTiet a " +
//            "WHERE a.gioHang.maGioHang = ?1 AND a.sanPhamChiTiet.maSanPhamCT = ?2")
//    void delete(Long maGH ,Long maSPCT);

}
