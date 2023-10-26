package com.example.websonserver.repository;

import com.example.websonserver.entity.VaiTro;
import com.example.websonserver.entity.VaiTroNguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro,Long > {
    Optional<VaiTro> findByTenVaiTro(VaiTroNguoiDung tenVaiTro);
}
