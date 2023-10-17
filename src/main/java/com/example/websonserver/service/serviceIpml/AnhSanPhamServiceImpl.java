package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.AnhSanPhamRequest;
import com.example.websonserver.entity.AnhSanPham;
import com.example.websonserver.repository.AnhSanPhamRepository;
import com.example.websonserver.service.AnhSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnhSanPhamServiceImpl implements AnhSanPhamService {
    @Autowired
    private AnhSanPhamRepository anhSanPhamRepository;
    @Override
    public AnhSanPham create(AnhSanPhamRequest anhSP) {
        AnhSanPham anhSanPham = anhSP.map(new AnhSanPham());
        return anhSanPhamRepository.save(anhSanPham);
    }
    @Override
    public AnhSanPham update(AnhSanPhamRequest anhSP, Long id) {
        Optional<AnhSanPham> optional = anhSanPhamRepository.findById(id);
        return optional.map(o->{
            o.setAnh(anhSP.getAnh());
            o.setTrangThai(anhSP.getTrangThai());
            o.setXoa(anhSP.getXoa());
            return anhSanPhamRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<AnhSanPham> getAll(Pageable pageable) {
        return anhSanPhamRepository.findAllByXoaFalse(pageable);
    }

    @Override
    @Transient
    public void delete(Long id) {
        anhSanPhamRepository.delete(id);
    }

    @Override
    public AnhSanPham findById(String id) {
        Optional<AnhSanPham>anhsp = anhSanPhamRepository.findById(Long.parseLong(id));
        AnhSanPham anhSanPham = anhsp.orElse(null);
        return anhSanPham;
    }

    @Override
    public List<String> getImagesBySanPhamChiTiet(Long maSanPhamCT) {
        List<String> imageUrls = anhSanPhamRepository.findImageUrlsBySanPhamChiTietId(maSanPhamCT);
        return imageUrls;
    }



}
