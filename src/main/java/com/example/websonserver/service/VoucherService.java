package com.example.websonserver.service;

import com.example.websonserver.dto.request.VoucherRequest;
import com.example.websonserver.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoucherService {;
    Page<Voucher> getAllVoucher(Pageable pageable);
    Voucher getVoucherById(Long id);
    Voucher saveVoucher(VoucherRequest Voucher);
    int deleteVoucher(Long id);
    Voucher update(Long id, VoucherRequest VoucherRequest);
}
