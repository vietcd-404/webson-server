package com.example.websonserver.service;

import com.example.websonserver.dto.request.VoucherRequest;
import com.example.websonserver.entity.Voucher;

import java.util.List;

public interface VoucherService {;
    List<Voucher> getAllVoucher();
    Voucher getVoucherById(Long id);
    Voucher saveVoucher(VoucherRequest Voucher);
    int deleteVoucher(Long id);
    Voucher update(Long id, VoucherRequest VoucherRequest);
}
