package com.example.websonserver.repository;

import com.example.websonserver.dto.response.ThongKeResponse.ThongTinThongKe;
import com.example.websonserver.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongKeRepository extends JpaRepository<HoaDonChiTiet,Long> {
    @Query("SELECT " +
            "   COALESCE(SUM(h.donGia * h.soLuong), 0)," +
            "   COALESCE(SUM(h.soLuong), 0) " +
            "FROM HoaDonChiTiet h " +
            "WHERE FUNCTION('YEAR', h.ngayTao) BETWEEN ?1 AND ?2 " +
            "   AND h.hoaDon.trangThai = 3 " +
            "   AND h.hoaDon.ngayThanhToan IS NOT NULL")
    List<Object[]> getDoanhThuTheoNam(Integer startYear, Integer endYear);


    @Query("SELECT " +
            "   SUM(h.donGia * h.soLuong)," +
            "   SUM(h.soLuong) " +
            "FROM HoaDonChiTiet h " +
            "WHERE FUNCTION('MONTH', h.ngayTao) = ?1 " +
            "   AND h.trangThai = 3 ")
    List<Object[]> getDoanhThuTheoThang(Integer month);

    @Query("SELECT " +
            "   SUM(h.donGia * h.soLuong)," +
            "   SUM(h.soLuong) " +
            "FROM HoaDonChiTiet h " +
            "WHERE FUNCTION('Day', h.ngayTao) = ?1 " +
            "   AND h.trangThai = 3 ")
    List<Object[]> getDoanhThuTheoNgay(Integer day);

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

}
