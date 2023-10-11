package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.NguoiDungRequest;
import com.example.websonserver.entity.*;
import com.example.websonserver.repository.NguoiDungRepository;
import com.example.websonserver.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class NguoiDungServiceImpl implements NguoiDungService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public NguoiDung create(NguoiDungRequest request){
        NguoiDung nguoiDung = null;
        try {
            nguoiDung = request.map(new NguoiDung());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nguoiDungRepository.save(nguoiDung);
    }

    @Override
    public NguoiDung update(NguoiDungRequest request, Long id) {
        Optional<NguoiDung> optional = nguoiDungRepository.findById(id);
        return optional.map(o->{
            o.setEmail(request.getEmail());
            o.setGioiTinh(Integer.parseInt(request.getGioiTinh()));
            o.setAnh(request.getAnh());
            o.setNgaySinh(request.getNgaySinh());
            o.setUsername(request.getUsername());
            o.setPassword(request.getPassword());
            o.setSdt(request.getSDT());
            o.setHo(request.getHo());
            o.setTenDem(request.getTenDem());
            o.setTen(request.getTen());
            o.setTrangThai(request.getTrangThai());
            o.setXoa(request.getXoa());
            return nguoiDungRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<NguoiDung> getAll(Pageable pageable) {
        return nguoiDungRepository.findAllByXoaFalse(pageable);
    }

    @Override
    public void delete(Long id) {
        nguoiDungRepository.delete(id);
    }

    @Override
    public NguoiDung findById(String id) {
        Optional<NguoiDung> nd = nguoiDungRepository.findById(Long.parseLong(id));
        NguoiDung nguoiDung = nd.orElse(null);
        return nguoiDung;
    }

    @Override
    public List<NguoiDung> searchByKeyword(String keyword) {
        return nguoiDungRepository.searchByKeyword(keyword);
    }
}
