package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.entity.VaiTro;
import com.example.websonserver.entity.VaiTroNguoiDung;
import com.example.websonserver.repository.VaiTroRepository;
import com.example.websonserver.service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VaiTroServiceImpl implements VaiTroService {


    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Override
    public Optional<VaiTro> findByRoleName(VaiTroNguoiDung vaiTroNguoiDung) {
        return vaiTroRepository.findByTenVaiTro(vaiTroNguoiDung);
    }
}
