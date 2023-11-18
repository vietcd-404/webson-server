package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.VoucherRequest;
import com.example.websonserver.entity.Voucher;
import com.example.websonserver.repository.VoucherRepository;
import com.example.websonserver.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    @Autowired
    public VoucherServiceImpl(VoucherRepository VoucherRepository) {
        this.voucherRepository = VoucherRepository;
    }


    @Override
    public List<Voucher> getAllVoucher() {
        return voucherRepository.findAllByXoaFalse();
    }

    @Override
    public Voucher getVoucherById(Long id) {
        return null;
    }

    @Override
    public Voucher saveVoucher(VoucherRequest Voucher) {
        Voucher km = Voucher.map(new Voucher());
        return voucherRepository.save(km);
    }

    @Override
    public int deleteVoucher(Long id) {
        return voucherRepository.delete(id);
    }

    @Override
    public Voucher update(Long id, VoucherRequest vcr) {
        Optional<Voucher> optional = voucherRepository.findByMaVoucher(id);
        return optional.map(o->{
            o.setTenVoucher(vcr.getTenVoucher());
            o.setKieuGiamGia(vcr.getKieuGiamGia());
            o.setGiaTriGiam(vcr.getGiaTriGiam());
            o.setThoiGianBatDau(vcr.getThoiGianBatDau());
            o.setThoiGianKetThuc(vcr.getThoiGianKetThuc());
            o.setSoLuong(vcr.getSoLuong());
            o.setMoTa(vcr.getMoTa());
            o.setTrangThai(vcr.getTrangThai());
            o.setXoa(vcr.getXoa());
            o.setDieuKien(vcr.getDieuKien());
            o.setGiamToiDa(vcr.getGiamToiDa());
            return voucherRepository.save(o);
        }).orElse(null);
    }
}
