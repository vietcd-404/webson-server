package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.NguoiDungRequest;
import com.example.websonserver.dto.response.NguoiDungResponse;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.entity.VaiTro;
import com.example.websonserver.entity.VaiTroNguoiDung;
import com.example.websonserver.service.NguoiDungService;
import com.example.websonserver.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class NguoiDungServiceImpl implements NguoiDungService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    private final PasswordEncoder passwordEncoder;

    public NguoiDungServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public NguoiDung create(NguoiDungRequest request) {
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setEmail(request.getEmail());
        nguoiDung.setGioiTinh(Integer.parseInt(request.getGioiTinh()));
        nguoiDung.setAnh(request.getAnh());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDateTime dateTime = LocalDateTime.parse(request.getNgaySinh(), formatter);
        nguoiDung.setNgaySinh(request.getNgaySinh());
        nguoiDung.setUsername((request.getUsername()));
        nguoiDung.setPassword(passwordEncoder.encode(request.getPassword()));
        nguoiDung.setSdt(request.getSdt());
        nguoiDung.setHo(request.getHo());
        nguoiDung.setTenDem(request.getTenDem());
        nguoiDung.setTen(request.getTen());
        nguoiDung.setXoa(request.getXoa());
        nguoiDung.setVaiTro(VaiTro.builder().maVaiTro(Long.valueOf(request.getVaiTro())).build());
        nguoiDung.setTrangThai(request.getTrangThai());


        return nguoiDungRepository.save(nguoiDung);
    }

    @Override
    public NguoiDung update(NguoiDungRequest request, Long id) {
        Optional<NguoiDung> optional = nguoiDungRepository.findById(id);

        return optional.map(o -> {
            o.setEmail(request.getEmail());
            o.setGioiTinh(Integer.parseInt(request.getGioiTinh()));
            o.setAnh(request.getAnh());
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDateTime dateTime = LocalDateTime.parse(request.getNgaySinh(), formatter);
            o.setNgaySinh(request.getNgaySinh());
            o.setUsername(request.getUsername());
            o.setPassword(passwordEncoder.encode(request.getPassword()));
            o.setSdt(request.getSdt());
            o.setHo(request.getHo());
            o.setTenDem(request.getTenDem());
            o.setTen(request.getTen());
            o.setTrangThai(request.getTrangThai());
            o.setXoa(request.getXoa());
            o.setVaiTro(VaiTro.builder().maVaiTro(Long.valueOf(request.getVaiTro())).build());
            return nguoiDungRepository.save(o);
        }).orElse(null);
    }

    @Override
    public List<NguoiDungResponse> getAll() {
        List<NguoiDung> nguoiDungList = this.nguoiDungRepository.findAllByXoaFalse();
        List<NguoiDungResponse> nguoiDungResponses = new ArrayList<>();

        for (NguoiDung nguoiDung : nguoiDungList) {
            NguoiDungResponse response = new NguoiDungResponse();
            response.setMaNguoiDung(nguoiDung.getMaNguoiDung());
            response.setNgaySinh(nguoiDung.getNgaySinh());
            response.setPassword(nguoiDung.getPassword());
            response.setEmail(nguoiDung.getEmail());
            response.setUsername(nguoiDung.getUsername());
            response.setSdt(nguoiDung.getSdt());
            response.setHo(nguoiDung.getHo());
            response.setTenDem(nguoiDung.getTenDem());
            response.setTen(nguoiDung.getTen());
            response.setGioiTinh(nguoiDung.getGioiTinh());
            response.setTrangThai(nguoiDung.getTrangThai());
            VaiTro vaiTro = nguoiDung.getVaiTro();
            if (vaiTro != null) {
                VaiTroNguoiDung tenVaiTro = vaiTro.getTenVaiTro();
                response.setVaiTro(tenVaiTro.name());
            }
            nguoiDungResponses.add(response);
        }
        return nguoiDungResponses;
//        return nguoiDungRepository.findAllByXoaFalse(pageable);
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

    @Override
    public NguoiDung findByUsername(String username) {
        return nguoiDungRepository.findByUsername(username);
    }

    @Override
    public Boolean existByUsername(String username) {
        return nguoiDungRepository.existsByUsername(username);
    }

    @Override
    public Boolean existByEmail(String email) {
        return nguoiDungRepository.existsByEmail(email);
    }

    @Override
    public NguoiDung saveOrUpdate(NguoiDung nguoiDung) {
        return nguoiDungRepository.save(nguoiDung);
    }

    public NguoiDung findByEmail(String email) {
        return nguoiDungRepository.findByEmail(email);
    }

    public List<NguoiDung> findUnactivatedAccounts(LocalDateTime cutoffTime) {
        int trangThai = 0;
        return nguoiDungRepository.findByTrangThaiAndAndNgayTaoBefore(trangThai, cutoffTime);
    }

    public boolean deleteUser(Long id) {
        nguoiDungRepository.deleteById(id);
        return true;
    }
}
