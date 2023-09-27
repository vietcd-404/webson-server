package com.example.websonserver.service;


import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.response.LoaiResponse;
import com.example.websonserver.entity.Loai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.beans.Transient;
import java.util.List;

public interface LoaiService {
    public Loai create(LoaiResquest loai);
    public Loai update(LoaiResquest loai,Long id);
    public Page<Loai> getAll(Pageable pageable);
    public void delete(Long id);
}
