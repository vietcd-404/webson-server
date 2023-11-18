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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Modifying
    @Query("UPDATE HoaDon a " +
            "SET a.trangThai = ?1 WHERE a.maHoaDon = ?2")
    void delete(Integer trangThai,Long maHoaDon);
}
