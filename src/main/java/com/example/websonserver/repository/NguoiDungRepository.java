package com.example.websonserver.repository;

import com.example.websonserver.entity.NguoiDung;
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
public interface NguoiDungRepository extends JpaRepository<NguoiDung,Long> {
    public Page<NguoiDung> findAllByXoaFalse(Pageable pageable);
    @Transactional
    @Modifying
    @Query("UPDATE NguoiDung a " +
            "SET a.xoa = true WHERE a.maNguoiDung = ?1")
    void delete(Long maNguoiDung);
    @Query("SELECT nd FROM NguoiDung nd WHERE nd.username LIKE %:keyword% " +
            "OR nd.ho LIKE %:keyword%")
    List<NguoiDung> searchByKeyword(@Param("keyword") String keyword);
}
