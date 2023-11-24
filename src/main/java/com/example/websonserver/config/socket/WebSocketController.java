package com.example.websonserver.config.socket;

import com.example.websonserver.dto.request.HoaDonRequest;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.entity.GioHang;
import com.example.websonserver.entity.HoaDon;
import com.example.websonserver.entity.Voucher;

import com.example.websonserver.repository.GioHangRepository;
import com.example.websonserver.repository.HoaDonRepository;

import com.example.websonserver.repository.VoucherRepository;
import com.example.websonserver.service.serviceIpml.HoaDonServiceIpml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebSocketController {


    @Autowired
    private HoaDonServiceIpml hoaDonServiceIpml;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @MessageMapping("/updateOrderStatus")
    public ResponseEntity<?> capNhapTrangThaiHoaDonByAdmin(UpdateStatus updateStatus) {
        HoaDon hoaDon = this.hoaDonRepository.findById(updateStatus.getMaHoaDon()).orElse(null);
        updateStatus.setTrangThai(hoaDon.getTrangThai());
        this.messagingTemplate.convertAndSend("/topic/orderStatus", updateStatus);
        return ResponseEntity.ok(hoaDonServiceIpml.updateStatus(updateStatus.getTrangThai(), updateStatus.getMaHoaDon()));
    }

    @MessageMapping("/newOrder")
    public ResponseEntity<?> placeOrder(NewOrder hoaDon) {
        HoaDonRequest request = new HoaDonRequest();
        hoaDon.setRequest(request);
        this.messagingTemplate.convertAndSend("/topic/orderStatus", hoaDon);
        return ResponseEntity.ok(hoaDonServiceIpml.placeOrder(hoaDon.getRequest(), hoaDon.getMaGioHang()));
    }

    @MessageMapping("/newGuest")
    public ResponseEntity<?> thanhToanGuest(NewOrder request) {
            this.messagingTemplate.convertAndSend("/topic/orderStatus", request);
            return ResponseEntity.ok(hoaDonServiceIpml.thanhToanGuest(request.getRequest(), request.getMa()));
    }

}
