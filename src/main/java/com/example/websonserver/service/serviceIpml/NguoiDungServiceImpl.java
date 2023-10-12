package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.NguoiDungRequest;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.service.NguoiDungService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NguoiDungServiceImpl implements NguoiDungService {
    @Override
    public NguoiDung findByUsername(String username) {
        return null;
    }

    @Override
    public Boolean existByUsername(String username) {
        return null;
    }

    @Override
    public Boolean existByEmail(String email) {
        return null;
    }

    @Override
    public NguoiDung saveOrUpdate(NguoiDung nguoiDung) {
        return null;
    }

    @Override
    public NguoiDung create(NguoiDungRequest request) {
        return null;
    }

    @Override
    public NguoiDung update(NguoiDungRequest request, Long id) {
        return null;
    }

    @Override
    public Page<NguoiDung> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
