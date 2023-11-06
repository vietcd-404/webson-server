package com.example.websonserver.repository;

import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Long> {
    public Page<SanPham> findAllByXoaFalse(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE SanPham a " +
            "SET a.xoa = true WHERE a.maSanPham = ?1")
    void delete(Long maSanPham);

    @Query("SELECT sp FROM SanPham sp WHERE sp.tenSanPham = ?1")
    SanPham findByTen(String tenSanPham);
}
