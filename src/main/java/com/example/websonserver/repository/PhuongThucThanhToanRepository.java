package com.example.websonserver.repository;

import com.example.websonserver.entity.PhuongThucThanhToan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface PhuongThucThanhToanRepository extends JpaRepository<PhuongThucThanhToan,Long> {
    public Page<PhuongThucThanhToan> findAllByXoaFalse(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE PhuongThucThanhToan a " +
            "SET a.xoa = true WHERE a.maPhuongThuc = ?1")
    void delete(Long maPhuongThuc);

}
