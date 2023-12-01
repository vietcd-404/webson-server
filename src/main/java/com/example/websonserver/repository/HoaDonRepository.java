package com.example.websonserver.repository;

import com.example.websonserver.dto.response.HoaDonResponse;
import com.example.websonserver.entity.GioHangChiTiet;
import com.example.websonserver.entity.HoaDon;
import com.example.websonserver.entity.HoaDonChiTiet;
import com.example.websonserver.entity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    Page<HoaDon> findAllByXoaFalseOrderByNgayTaoDesc(Pageable pageable);

    Page<HoaDon> findAllByXoaFalseAndTrangThaiOrderByNgayTaoDesc(Integer status, Pageable pageable);

    List<HoaDon> findByNguoiDungAndTrangThai(NguoiDung nguoiDung, Integer trangThai);

    List<HoaDon> findHoaDonByNguoiDung(NguoiDung nguoiDung);

    List<HoaDon> findByNguoiDung(NguoiDung nguoiDung, Sort sort);

    Optional<HoaDon> findByNguoiDungAndMaHoaDon(NguoiDung nguoiDung, Long maHoaDon);

    List<HoaDon> findByMaHoaDon(Long maHoaDon);


    List<HoaDon> findByMaHoaDonAndTrangThaiAndXoaIsFalse(Long maHoaDon,Integer trangThai);

    @Transactional
    @Modifying
    @Query("UPDATE HoaDon a " +
            "SET a.trangThai = ?1 WHERE a.maHoaDon = ?2")
    void delete(Integer trangThai,Long maHoaDon);

    @Query(value = "SELECT hct.ma_san_pham_chi_tiet, SUM(hct.so_luong) AS soLuongBan\n" +
            "FROM hoa_don hd\n" +
            "JOIN hoa_don_chi_tiet hct ON hd.ma_hoa_don = hct.ma_hoa_don\n" +
            "WHERE hd.trang_thai = 3\n" +
            "GROUP BY hct.ma_san_pham_chi_tiet\n" +
            "ORDER BY soLuongBan DESC\n" +
            "LIMIT 4;\n", nativeQuery = true)
    List<Object[]> top4BestSeller();

    @Query(value = "select sum(hd.tong_tien) as tongTien from hoa_don hd where hd.trang_thai=3",nativeQuery = true)
    BigDecimal sumTotalBill();

    @Query(value = "SELECT hd.nguoiDung, COUNT(hd) AS totalOrders " +
            "FROM HoaDon hd " +
            "WHERE hd.trangThai = 3 " +
            "GROUP BY hd.nguoiDung " +
            "ORDER BY totalOrders DESC ")
    List<Object[]> findTop4Buyers();

    @Query("SELECT nd FROM HoaDon nd WHERE " +
            "(nd.nguoiDung.ho LIKE %:keyword% OR " +
            "nd.nguoiDung.ten LIKE %:keyword% OR " +
            "nd.nguoiDung.tenDem LIKE %:keyword%) " +
            "AND nd.xoa = false " +
            "AND nd.trangThai = :trangThai " +
            "ORDER BY nd.ngayTao DESC")
    List<HoaDon> searchByHoTen(@Param("keyword") String keyword, @Param("trangThai") Integer trangThai);


}
