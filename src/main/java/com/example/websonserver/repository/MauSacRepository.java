package com.example.websonserver.repository;

import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac,Long> {
    public Page<MauSac> findAllByXoaFalse(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE MauSac a " +
            "SET a.xoa = true WHERE a.maMau = ?1")
    void delete(Long maMau);
}
