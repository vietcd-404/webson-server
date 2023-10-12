package com.example.websonserver.service;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.PhuongThucThanhToanRequest;
import com.example.websonserver.entity.PhuongThucThanhToan;
import com.example.websonserver.entity.PhuongThucThanhToan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhuongThucThanhToanService{
    public PhuongThucThanhToan create(PhuongThucThanhToanRequest request);
    public PhuongThucThanhToan update(PhuongThucThanhToanRequest request,Long id);
    public Page<PhuongThucThanhToan> getAll(Pageable pageable);
    public void delete(Long id);

}
