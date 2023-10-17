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

    public List<AnhSanPham> findAllByXoaFalse();

    @Query("SELECT a.anh FROM AnhSanPham a WHERE a.sanPhamChiTiet.maSanPhamCT = :maSanPhamCT")
    List<String> findImageUrlsBySanPhamChiTietId(@Param("maSanPhamCT") Long maSanPhamCT);

    @Transactional
    @Modifying
    @Query("UPDATE AnhSanPham a " +
            "SET a.xoa = true WHERE a.maAnh = ?1")
    void delete(Long maAnh);
}
