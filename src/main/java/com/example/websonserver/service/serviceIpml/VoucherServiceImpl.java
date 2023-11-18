package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.VoucherRequest;
import com.example.websonserver.entity.Voucher;
import com.example.websonserver.repository.VoucherRepository;
import com.example.websonserver.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.example.websonserver.constants.Constants.STATUS_VOUCHER.*;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    @Autowired
    public VoucherServiceImpl(VoucherRepository VoucherRepository) {
        this.voucherRepository = VoucherRepository;
    }


    @Override
    public List<Voucher> getAllVoucher() {
        updateStatus();
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
            return voucherRepository.save(o);
        }).orElse(null);
    }
    public void updateStatus() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedTime = LocalDateTime.now().format(formatter);
        List<Voucher> listVoucher = getAllVoucher();
        for (Voucher x : listVoucher) {
            Voucher voucher = getVoucherById(x.getMaVoucher());
            LocalDateTime startTime = voucher.getThoiGianBatDau();
            LocalDateTime endTime = voucher.getThoiGianKetThuc();
            if (LocalDate.parse(formattedTime, formatter).isBefore(startTime.toLocalDate()) ||
                    LocalDate.parse(formattedTime, formatter).isAfter(endTime.toLocalDate())) {
                voucher.setTrangThai(KHONG_HOAT_DONG);
            } else {
                voucher.setTrangThai(HOAT_DONG);
            }
            voucherRepository.save(voucher);
        }
    }
    @Scheduled(cron = "0 0 0 * * *") // Chạy mỗi ngày lúc 00:00:00
    public void scheduledUpdateStatus() {
        updateStatus();
    }
}
