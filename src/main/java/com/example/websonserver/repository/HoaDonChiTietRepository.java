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
    List<HoaDonChiTiet> findByHoaDon_MaHoaDon(Long maHoaDon);

    List<HoaDonChiTiet> findByHoaDon(HoaDon hoaDon);

    List<HoaDonChiTiet> findBySanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet);

    HoaDonChiTiet findByHoaDonAndSanPhamChiTiet(HoaDon hoaDon, SanPhamChiTiet sanPhamChiTiet);

    @Query(value = "SELECT hdct.ma_san_pham_chi_tiet, SUM(hdct.so_luong) AS tongSL " +
            "FROM hoa_don_chi_tiet hdct " +
            "WHERE (hdct.xoa = false OR hdct.xoa IS NULL)" +
            "GROUP BY hdct.ma_san_pham_chi_tiet " +
            "ORDER BY tongSL DESC " +
            "LIMIT 4", nativeQuery = true)
    List<Object[]> findTop4BanChay();

}
