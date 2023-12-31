package com.example.websonserver.repository;

import com.example.websonserver.entity.AnhSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AnhSanPhamRepository extends JpaRepository<AnhSanPham,Long> {
    public Page<AnhSanPham> findAllByXoaFalse(Pageable pageable);



    @Query("SELECT a FROM AnhSanPham a WHERE a.xoa = false AND a.trangThai = 0")
    public Page<AnhSanPham> getAll(Pageable pageable);



    @Query("SELECT a.anh FROM AnhSanPham a WHERE a.sanPhamChiTiet.maSanPhamCT = :maSanPhamCT AND a.trangThai = 1")
    List<String> findImageUrlsBySanPhamChiTietId(@Param("maSanPhamCT") Long maSanPhamCT);

    @Query("SELECT a FROM AnhSanPham a WHERE a.sanPhamChiTiet.maSanPhamCT = :maSanPhamCT AND a.trangThai = 1")
    List<AnhSanPham> findImage(@Param("maSanPhamCT") Long maSanPhamCT);

    @Transactional
    @Modifying
    @Query("UPDATE AnhSanPham a " +
            "SET a.xoa = true WHERE a.maAnh = ?1")
    void delete(Long maAnh);

    @Transactional
    @Modifying
    @Query("UPDATE AnhSanPham a " +
            "SET a.trangThai = 0 WHERE a.maAnh = ?1")
    void deleteAnh(Long maAnh);
}
