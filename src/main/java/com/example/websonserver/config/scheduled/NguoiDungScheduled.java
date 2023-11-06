package com.example.websonserver.config.scheduled;


import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.service.serviceIpml.NguoiDungServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Log4j2
@EnableScheduling
public class NguoiDungScheduled {
    @Autowired
    private NguoiDungServiceImpl nguoiDungService;

    @Scheduled(fixedDelay = 4 * 60 * 1000)
    public void deleteUnactivatedAccounts() {

        log.info("Xóa người dùng :: Start");
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(4);
        List<NguoiDung> xoaNguoiDungChuaKichHoat = nguoiDungService.findUnactivatedAccounts(cutoffTime);

        xoaNguoiDungChuaKichHoat.stream()
                .filter(user -> user.getTrangThai() == 0)
                .forEach(user -> nguoiDungService.delete(user.getMaNguoiDung()));
        log.info("Xóa người dùng :: End");
    }
}
