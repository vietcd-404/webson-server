package com.example.websonserver.repository;

import com.example.websonserver.entity.DiaChi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi,Long> {
    public Page<DiaChi> findAllByXoaFalse(Pageable pageable);
    @Transactional
    @Modifying
    @Query("UPDATE DiaChi a " +
            "SET a.xoa = true WHERE a.maDiaChi = ?1")
    void delete(Long maDiaChi);
}
