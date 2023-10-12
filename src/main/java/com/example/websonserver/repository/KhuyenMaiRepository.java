package com.example.websonserver.repository;

import com.example.websonserver.entity.KhuyenMai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai,Long> {
        List<KhuyenMai> findAllByXoaFalse();
        Optional<KhuyenMai> findByMaKhuyenMai(Long id);

        @Transactional
        @Modifying
        @Query("UPDATE KhuyenMai a " +
                "SET a.xoa = true WHERE a.maKhuyenMai = ?1")
        int delete(Long ma);
}
