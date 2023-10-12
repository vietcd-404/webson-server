package com.example.websonserver.repository;

import com.example.websonserver.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet,Long> {
    public Page<SanPhamChiTiet> findAllByXoaFalse(Pageable pageable);
    @Transactional
    @Modifying
    @Query("UPDATE SanPhamChiTiet a " +
            "SET a.xoa = true WHERE a.maSanPhamCT = ?1")
    void delete(Long maSanPhamCT);
}
