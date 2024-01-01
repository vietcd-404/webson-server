package com.example.websonserver.repository;

import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.MauSac;
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
public interface MauSacRepository extends JpaRepository<MauSac,Long> {
    public Page<MauSac> findAllByXoaFalseOrderByNgayTaoDesc(Pageable pageable);
    Boolean existsByTenMauAndXoaFalse(String tenMau);
    @Transactional
    @Modifying
    @Query("UPDATE MauSac a " +
            "SET a.xoa = true WHERE a.maMau = ?1")
    void delete(Long maMau);

    @Query("SELECT sp FROM MauSac sp WHERE sp.tenMau = ?1 and sp.xoa=false")
    MauSac findByTen(String tenMau);

    @Query("SELECT m FROM MauSac m WHERE m.xoa = false and m.trangThai =1  order by  m.ngayTao desc ")
    public List<MauSac> fillComboSpctByNMau();
}
