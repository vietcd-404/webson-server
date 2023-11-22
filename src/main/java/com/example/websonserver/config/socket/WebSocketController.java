package com.example.websonserver.config.socket;

import com.example.websonserver.entity.HoaDon;
import com.example.websonserver.repository.HoaDonRepository;
import com.example.websonserver.service.serviceIpml.HoaDonServiceIpml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {


    @Autowired
    private HoaDonServiceIpml hoaDonServiceIpml;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/updateOrderStatus")
    public ResponseEntity<?> capNhapTrangThaiHoaDonByAdmin(UpdateStatus updateStatus) {
        HoaDon hoaDon = this.hoaDonRepository.findById(updateStatus.getMaHoaDon()).orElse(null);
        updateStatus.setTrangThai(hoaDon.getTrangThai());
        this.messagingTemplate.convertAndSend("/topic/orderStatus", updateStatus);
        return ResponseEntity.ok(hoaDonServiceIpml.updateStatus(updateStatus.getTrangThai(), updateStatus.getMaHoaDon()));
    }

}
