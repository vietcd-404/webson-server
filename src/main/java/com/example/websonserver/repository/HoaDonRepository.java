package com.example.websonserver.repository;

import com.example.websonserver.dto.response.HoaDonResponse;
import com.example.websonserver.entity.HoaDon;
import com.example.websonserver.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon,Long> {
    List<HoaDon> findByNguoiDungAndTrangThai(NguoiDung nguoiDung, Integer trangThai);
}
