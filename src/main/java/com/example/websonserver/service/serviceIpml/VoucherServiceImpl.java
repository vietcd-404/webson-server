package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.VoucherRequest;
import com.example.websonserver.entity.Voucher;
import com.example.websonserver.repository.VoucherRepository;
import com.example.websonserver.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.example.websonserver.constants.Constants.STATUS_VOUCHER.*;

@Service
@EnableScheduling
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

    public List<Voucher> getAllVoucherUser() {
        return voucherRepository.findAllByXoaFalseAndTrangThai(0);
    }

    @Override
    public Voucher getVoucherById(Long id) {
        return null;
    }

    @Override
    public Voucher saveVoucher(VoucherRequest voucher) {
        Voucher km = voucher.map(new Voucher());
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
            o.setGiaTriGiam(vcr.getGiaTriGiam());
            LocalDateTime newThoiGianBatDau = vcr.getThoiGianBatDau().withHour(o.getThoiGianBatDau().getHour()).withMinute(o.getThoiGianBatDau().getMinute())
                    .withSecond(o.getThoiGianBatDau().getSecond());
            o.setThoiGianBatDau(newThoiGianBatDau);
            LocalDateTime newThoiGianKetThuc = vcr.getThoiGianKetThuc().withHour(o.getThoiGianKetThuc().getHour()).withMinute(o.getThoiGianKetThuc().getMinute())
                    .withSecond(o.getThoiGianKetThuc().getSecond());
            o.setThoiGianKetThuc(newThoiGianKetThuc);
            o.setSoLuong(vcr.getSoLuong());
            o.setMoTa(vcr.getMoTa());
            o.setTrangThai(vcr.getTrangThai());
            o.setXoa(vcr.getXoa());
            o.setDieuKien(vcr.getDieuKien());
            o.setGiamToiDa(vcr.getGiamToiDa());
            return voucherRepository.save(o);
        }).orElse(null);
    }
    public void updateStatus() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedTime = LocalDateTime.now().format(formatter);
        List<Voucher> listVoucher = getAllVoucher();
        for (Voucher x : listVoucher) {
            LocalDateTime startTime = x.getThoiGianBatDau();
            LocalDateTime endTime = x.getThoiGianKetThuc();
            if (LocalDate.parse(formattedTime, formatter).isBefore(startTime.toLocalDate()) ||
                    LocalDate.parse(formattedTime, formatter).isAfter(endTime.toLocalDate())) {
                x.setTrangThai(KHONG_HOAT_DONG);
            } else {
                x.setTrangThai(HOAT_DONG);
            }
            voucherRepository.save(x);
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // Chạy mỗi ngày lúc 00:00:00
    public void scheduledUpdateStatus() {
        updateStatus();
    }
//    @Scheduled(fixedRate = 2000) // Chạy mỗi 2 giây
//    public void scheduledUpdateStatus() {
//        updateStatus();
//    }
}
