package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.PhuongThucThanhToanRequest;
import com.example.websonserver.entity.Loai;
import com.example.websonserver.entity.PhuongThucThanhToan;
import com.example.websonserver.repository.PhuongThucThanhToanRepository;
import com.example.websonserver.service.PhuongThucThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhuongThucThanhToanServiceImpl implements PhuongThucThanhToanService {
    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Override
    public PhuongThucThanhToan create(PhuongThucThanhToanRequest request) {
        PhuongThucThanhToan phuongThucThanhToan = request.map(new PhuongThucThanhToan());
        return phuongThucThanhToanRepository.save(phuongThucThanhToan);
    }

    @Override
    public PhuongThucThanhToan update(PhuongThucThanhToanRequest request, Long id) {
        Optional<PhuongThucThanhToan> optional = phuongThucThanhToanRepository.findById(id);
        return optional.map(o->{
            o.setTenPhuongThuc(request.getTenPhuongThuc());
            return phuongThucThanhToanRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<PhuongThucThanhToan> getAll(Pageable pageable) {
        return phuongThucThanhToanRepository.findAllByXoaFalse(pageable);
    }

    @Override
    public void delete(Long id) {
        phuongThucThanhToanRepository.delete(id);
    }
}
