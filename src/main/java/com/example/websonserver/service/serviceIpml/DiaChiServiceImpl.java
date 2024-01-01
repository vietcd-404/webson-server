package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.DiaChiRequest;
import com.example.websonserver.dto.response.DiaChiResponse;
import com.example.websonserver.entity.*;
import com.example.websonserver.repository.DiaChiRepository;
import com.example.websonserver.repository.NguoiDungRepository;
import com.example.websonserver.service.DiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DiaChiServiceImpl implements DiaChiService {
    @Autowired
    DiaChiRepository diaChiRepository;

    @Autowired
    private NguoiDungServiceImpl nguoiDungService;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public DiaChi create(DiaChiRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        NguoiDung nguoiDung = nguoiDungRepository.findByUsername(username);
        DiaChi diaChi = new DiaChi();
        diaChi.setDiaChi(request.getDiaChi());
        diaChi.setSdt(request.getSdt());
        diaChi.setNguoiDung(nguoiDung);
        diaChi.setLoaiDiaChi(request.getLoaiDiaChi());
        diaChi.setHuyen(request.getHuyen());
        diaChi.setXa(request.getXa());
        diaChi.setTinh(request.getTinh());
        diaChi.setXoa(request.getXoa());
        diaChi.setTrangThai(request.getTrangThai());
        return diaChiRepository.save(diaChi);
    }

    @Override
    public DiaChi update(DiaChiRequest request, Long id) {
        Optional<DiaChi> optional = diaChiRepository.findById(id);
        return optional.map(o -> {
            o.setDiaChi(request.getDiaChi());
            o.setLoaiDiaChi(request.getLoaiDiaChi());
            o.setHuyen(request.getHuyen());
            o.setXa(request.getXa());
            o.setTinh(request.getTinh());
            o.setSdt(request.getSdt());
            o.setTrangThai(request.getTrangThai());
            o.setXoa(request.getXoa());
            return diaChiRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<DiaChi> getAll(Pageable pageable) {
        return diaChiRepository.findAllByXoaFalse(pageable);
    }


    public List<DiaChiResponse> getDiaChiTheoNguoiDung(Principal principal) {
        NguoiDung nguoiDung = nguoiDungService.findByUsername(principal.getName());
        List<DiaChi> diaChiList = diaChiRepository.findByNguoiDung(nguoiDung);
        List<DiaChiResponse> diaChiResponses = new ArrayList<>();
        for (DiaChi diaChi : diaChiList) {
            DiaChiResponse dto = new DiaChiResponse();
            dto.setDiaChi(diaChi.getDiaChi());
            dto.setMaDiaChi(diaChi.getMaDiaChi());
            dto.setSdt(diaChi.getSdt());
            dto.setTinh(diaChi.getTinh());
            dto.setHuyen(diaChi.getHuyen());
            dto.setXa(diaChi.getXa());
            dto.setTrangThai(diaChi.getTrangThai());
            dto.setXoa(diaChi.getXoa());
            dto.setLoaiDiaChi(diaChi.getLoaiDiaChi());
            diaChiResponses.add(dto);
        }
        return diaChiResponses;
    }


    @Override
    public void delete(Long id) {
        diaChiRepository.deleteById(id);
    }

    @Override
    public DiaChi findById(String id) {
        Optional<DiaChi> dc = diaChiRepository.findById(Long.parseLong(id));
        DiaChi diaChi = dc.orElse(null);
        return diaChi;
    }
}
