package com.example.websonserver.repository;

import com.example.websonserver.entity.AnhSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface AnhSanPhamRepository extends JpaRepository<AnhSanPham,Long> {
    public Page<AnhSanPham> findAllByXoaFalse(Pageable pageable);
    @Transactional
    @Modifying
    @Query("UPDATE AnhSanPham a " +
            "SET a.xoa = true WHERE a.maAnh = ?1")
    void delete(Long maAnh);
}
