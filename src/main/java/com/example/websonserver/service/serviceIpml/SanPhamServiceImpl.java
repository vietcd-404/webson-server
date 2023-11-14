package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.SanPhamRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.SanPham;
import com.example.websonserver.repository.LoaiRepository;
import com.example.websonserver.repository.SanPhamRepository;
import com.example.websonserver.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class SanPhamServiceImpl implements SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Override
    public SanPham create(SanPhamRequest sanPham) {
        SanPham sanPham1 = sanPham.map(new SanPham());
        return sanPhamRepository.save(sanPham1);
    }

    @Override
    public SanPham update(SanPhamRequest sanPham,Long id) {
        Optional<SanPham> optional = sanPhamRepository.findById(id);
        return optional.map(o->{
            o.setTenSanPham(sanPham.getTenSanPham());
            return sanPhamRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<SanPham> getAll(Pageable pageable) {
        return sanPhamRepository.findAllByXoaFalseOrderByNgayTaoDesc(pageable);
    }

    @Override
    @Transient
    public void delete(Long id) {
        sanPhamRepository.delete(id);
    }

    @Override
    public SanPham findByTen(String tenSP) {
        return sanPhamRepository.findByTen(tenSP);
    }

    @Override
    public SanPham updateStatus(UpdateTrangThai trangThai, Long id) {
        Optional<SanPham> optional = sanPhamRepository.findById(id);
        return optional.map(o -> {
            o.setTrangThai(trangThai.getTrangThai());
            return sanPhamRepository.save(o);
        }).orElse(null);
    }

    @Override
    public List<SanPham> fillComboSpctBySanPham() {
        return sanPhamRepository.fillComboSpctBySanPham();
    }
}
