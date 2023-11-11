package com.example.websonserver.repository;

import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.SanPham;
import com.example.websonserver.entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Long> {
    public Page<ThuongHieu> findAllByXoaFalseOrderByNgayTaoDesc(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE ThuongHieu a " +
            "SET a.xoa = true WHERE a.maThuongHieu = ?1")
    void delete(Long maThuongHieu);

    @Query("SELECT sp FROM ThuongHieu sp WHERE sp.tenThuongHieu = ?1")
    ThuongHieu findByTen(String tenThuongHieu);

    @Query("SELECT sp FROM ThuongHieu sp WHERE sp.xoa = false and sp.trangThai =1  order by  sp.ngayTao desc ")
    public List<ThuongHieu> fillComboSpct();
}
