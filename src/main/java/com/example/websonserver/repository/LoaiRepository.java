package com.example.websonserver.repository;

import com.example.websonserver.dto.response.LoaiResponse;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.ThuongHieu;
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
public interface LoaiRepository extends JpaRepository<Loai,Long> {

    public Page<Loai> findAllByXoaFalseOrderByNgayTaoDesc(Pageable pageable);

    @Query("SELECT sp FROM Loai sp WHERE sp.xoa = false and sp.trangThai =1  order by  sp.ngayTao desc ")
    public List<Loai> fillComboSpct();

    @Transactional
    @Modifying
    @Query("UPDATE Loai a " +
            "SET a.xoa = true WHERE a.maLoai = ?1")
    void delete(Long maLoai);

    @Query("SELECT sp FROM Loai sp WHERE sp.tenLoai = ?1")
    Loai findByTen(String tenLoai);
}
