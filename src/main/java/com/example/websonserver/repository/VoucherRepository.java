package com.example.websonserver.repository;


import com.example.websonserver.entity.Voucher;
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
public interface VoucherRepository extends JpaRepository<Voucher,Long> {
        List<Voucher> findAllByXoaFalse();
        public Page<Voucher> findAllByXoaFalseOrderByMaVoucherDesc(Pageable pageable);

        Optional<Voucher> findByMaVoucher(Long id);

        @Transactional
        @Modifying
        @Query("UPDATE Voucher a " +
                "SET a.xoa = true WHERE a.maVoucher = ?1")
        int delete(Long ma);
}
