package com.example.websonserver.repository;

import com.example.websonserver.dto.response.ThongKeResponse.ThongTinThongKe;
import com.example.websonserver.entity.HoaDonChiTiet;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ThongKeRepository extends JpaRepository<HoaDonChiTiet,Long> {
    @Query("SELECT " +
            "   COALESCE(SUM(DISTINCT h.hoaDon.tongTien), 0)," +
            "   COALESCE(SUM(h.soLuong), 0) " +
            "FROM HoaDonChiTiet h " +
            "WHERE FUNCTION('YEAR', h.hoaDon.ngayTao) =?1" +
            "   AND h.hoaDon.trangThai = 3 " +
            "   AND h.hoaDon.ngayThanhToan IS NOT NULL")
    List<Object[]> getDoanhThuTheoNam(Integer year);



    @Query("SELECT " +
            "   COALESCE(SUM(DISTINCT h.hoaDon.tongTien), 0)," +
            "   COALESCE(SUM(h.soLuong), 0) " +
            "FROM HoaDonChiTiet h " +
            "WHERE FUNCTION('MONTH', h.hoaDon.ngayTao) = ?1 " +
            "AND  FUNCTION('YEAR', h.hoaDon.ngayTao) = ?2 " +
            "   AND h.hoaDon.trangThai = 3 ")
    List<Object[]> getDoanhThuTheoThang(Integer month, Integer year);


    @Query(value = "SELECT " +
            "   COALESCE(SUM(DISTINCT hd.tong_tien), 0) AS tong_tien," +
            "   COALESCE(SUM(h.so_luong), 0) AS so_luong " +
            "FROM hoa_don_chi_tiet h " +
            "INNER JOIN hoa_don hd ON h.ma_hoa_don = hd.ma_hoa_don " +
            "WHERE hd.trang_thai = 3 AND DATE (hd.ngay_tao)= ?1 ", nativeQuery = true)
    List<Object[]> getDoanhThuTheoNgay(LocalDate ngayTao);




    @Query("SELECT COUNT(hdct) FROM HoaDonChiTiet hdct WHERE FUNCTION('DAY', hdct.ngayTao) = :day AND hdct.trangThai = :status")
    List<Object[]> getSoLuongHoaDonTheoTrangThaiVaNgay(@Param("day") Integer day, @Param("status") Integer status);

    @Query("SELECT COUNT(hdct) FROM HoaDonChiTiet hdct WHERE FUNCTION('MONTH', hdct.ngayTao) = :thang AND hdct.trangThai = :status")
    List<Object[]> getSoLuongHoaDonTheoTrangThaiVaThang(@Param("thang") Integer thang, @Param("status") Integer status);

    @Query("SELECT COUNT(hdct) FROM HoaDonChiTiet hdct WHERE FUNCTION('YEAR', hdct.ngayTao) = :nam AND hdct.trangThai = :status")
    List<Object[]> getSoLuongHoaDonTheoTrangThaiVaNam(@Param("nam") Integer nam, @Param("status") Integer status);

    @Query("SELECT " +
            "   SUM(h.donGia * h.soLuong)," +
            "   SUM(h.soLuong) " +
            "FROM HoaDonChiTiet h " +
            "WHERE FUNCTION('MONTH', h.ngayTao) = 01 " +
            "OR FUNCTION('MONTH', h.ngayTao) = 02 " +
            "OR FUNCTION('MONTH', h.ngayTao) = 03 " +
            "   AND h.trangThai = 3 ")
    List<Object[]> getDoanhThuTheoQuy1();

    @Query("SELECT " +
            "   SUM(h.donGia * h.soLuong)," +
            "   SUM(h.soLuong) " +
            "FROM HoaDonChiTiet h " +
            "WHERE FUNCTION('MONTH', h.ngayTao) = 04 " +
            "OR FUNCTION('MONTH', h.ngayTao) = 05 " +
            "OR FUNCTION('MONTH', h.ngayTao) = 06 " +
            "   AND h.trangThai = 3 ")
    List<Object[]> getDoanhThuTheoQuy2();

    @Query("SELECT " +
            "   SUM(h.donGia * h.soLuong)," +
            "   SUM(h.soLuong) " +
            "FROM HoaDonChiTiet h " +
            "WHERE FUNCTION('MONTH', h.ngayTao) = 07 " +
            "OR FUNCTION('MONTH', h.ngayTao) = 08 " +
            "OR FUNCTION('MONTH', h.ngayTao) = 09 " +
            "   AND h.trangThai = 3 ")
    List<Object[]> getDoanhThuTheoQuy3();

    @Query("SELECT " +
            "   SUM(h.donGia * h.soLuong)," +
            "   SUM(h.soLuong) " +
            "FROM HoaDonChiTiet h " +
            "WHERE FUNCTION('MONTH', h.ngayTao) = 10 " +
            "OR FUNCTION('MONTH', h.ngayTao) = 11 " +
            "OR FUNCTION('MONTH', h.ngayTao) = 12 " +
            "   AND h.trangThai = 3 ")
    List<Object[]> getDoanhThuTheoQuy4();

    @Query("SELECT COUNT(DISTINCT h.hoaDon) FROM HoaDonChiTiet h WHERE h.hoaDon.trangThai = :status")
    Long countByTrangThai(Integer status);

}
