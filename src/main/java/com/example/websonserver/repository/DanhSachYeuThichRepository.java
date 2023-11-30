package com.example.websonserver.repository;

import com.example.websonserver.dto.response.DanhSachYTResponse;
import com.example.websonserver.entity.DanhSachYeuThich;
import com.example.websonserver.entity.GioHangChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhSachYeuThichRepository extends JpaRepository<DanhSachYeuThich,Long> {
    Page<DanhSachYeuThich> findAllByXoaFalseOrderByNgayTaoDesc(Pageable pageable);

    @Query("SELECT ds FROM DanhSachYeuThich ds WHERE ds.nguoiDung.maNguoiDung = ?1 order by ds.ngayTao desc ")
    Page<DanhSachYeuThich> findByMaNguoiDung(Long ma, Pageable pageable);

    @Query("SELECT ds FROM DanhSachYeuThich ds WHERE ds.nguoiDung.maNguoiDung = ?1 and ds.sanPhamChiTiet.maSanPhamCT= ?2")
    DanhSachYeuThich findByUserAndProduct (Long maNguoiDung, Long maSanPhamCT);

    @Query("SELECT dsyt.sanPhamChiTiet.maSanPhamCT, COUNT(dsyt.maDanhSach) AS luotYeuThich " +
            "FROM DanhSachYeuThich dsyt " +
            "GROUP BY dsyt.sanPhamChiTiet.maSanPhamCT " +
            "ORDER BY luotYeuThich DESC")
    List<Object[]> thongKeTopSanPhamYeuThich(Pageable pageable);
}
