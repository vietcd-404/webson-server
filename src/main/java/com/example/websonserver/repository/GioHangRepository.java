package com.example.websonserver.repository;

import com.example.websonserver.entity.GioHang;
import com.example.websonserver.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang,Long> {
    public Page<GioHang> findAllByXoaFalse(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE GioHang a " +
            "SET a.xoa = true WHERE a.maGioHang = ?1")
    void delete(Long maGioHang);

    @Query("SELECT gh FROM GioHang gh WHERE gh.nguoiDung.email = ?1 AND gh.maGioHang = ?2")
    GioHang findCartByEmailAndCartId(String email, Long ma);

    @Query("SELECT gh FROM GioHang gh WHERE gh.nguoiDung.maNguoiDung = ?1")
    GioHang findByMaNguoiDung(Long maNguoiDung);

//    @Query("SELECT gh FROM GioHang gh JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id = ?1")
//    List<GioHang> findCartsByProductId(Long productId);
}
