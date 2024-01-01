package com.example.websonserver.repository;

import com.example.websonserver.entity.DiaChi;
import com.example.websonserver.entity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi,Long> {
    public Page<DiaChi> findAllByXoaFalse(Pageable pageable);
    DiaChi findByAndNguoiDung(NguoiDung nguoiDung);
    List<DiaChi> findByNguoiDung(NguoiDung nguoiDung);

    @Transactional
    @Modifying
    @Query("UPDATE DiaChi a " +
            "SET a.xoa = true WHERE a.maDiaChi = ?1")
    void delete(Long maDiaChi);
}
