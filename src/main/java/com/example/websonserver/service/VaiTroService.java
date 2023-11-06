package com.example.websonserver.service;

import com.example.websonserver.entity.VaiTro;
import com.example.websonserver.entity.VaiTroNguoiDung;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface VaiTroService {
    Optional<VaiTro> findByRoleName(VaiTroNguoiDung vaiTroNguoiDung);
}
