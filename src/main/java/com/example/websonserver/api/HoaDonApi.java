package com.example.websonserver.api;

import com.example.websonserver.config.socket.NewOrder;
import com.example.websonserver.config.socket.UpdateStatus;
import com.example.websonserver.dto.request.HoaDonRequest;
import com.example.websonserver.dto.request.NguoiDungSessionRequest;
import com.example.websonserver.dto.request.SanPhamHoaDonRequest;
import com.example.websonserver.dto.request.UpdateHoaDonRequest;
import com.example.websonserver.dto.response.GioHangChiTietResponse;
import com.example.websonserver.dto.response.HoaDonResponse;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.dto.response.ThanhToanRes;
import com.example.websonserver.entity.*;
import com.example.websonserver.jwt.JwtTokenProvider;
import com.example.websonserver.repository.GioHangRepository;
import com.example.websonserver.repository.HoaDonRepository;
import com.example.websonserver.repository.PhuongThucThanhToanRepository;
import com.example.websonserver.repository.VoucherRepository;
import com.example.websonserver.service.GioHangCTSessionService;
import com.example.websonserver.service.HoaDonService;
import com.example.websonserver.service.UserDetailService;
import com.example.websonserver.service.serviceIpml.HoaDonServiceIpml;
import com.example.websonserver.service.serviceIpml.VnPayServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class HoaDonApi {
    @Autowired
    private HoaDonServiceIpml hoaDonService;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VnPayServiceImpl vnPayService;

    @Autowired
    private GioHangCTSessionService gioHangCTSessionService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/user/order/place/{maGioHang}")
    public ResponseEntity<?> placeOrder(@RequestBody HoaDonRequest hoaDon, @PathVariable Long maGioHang) {
        GioHang gioHang = gioHangRepository.findById(maGioHang).orElse(null);
        Voucher voucher = voucherRepository.findByTenVoucher(hoaDon.getTenVoucher());
        try {
            if (gioHang == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Giỏ hàng không tồn tại"));
            }
            NewOrder newOrder = new NewOrder();
            newOrder.setMaGioHang(maGioHang);
            newOrder.setRequest(hoaDon);

            this.messagingTemplate.convertAndSend("/topic/orderStatus", newOrder);
            return ResponseEntity.ok(hoaDonService.placeOrder(hoaDon, maGioHang));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }
    }


    @PutMapping("/user/order/update/{maDonHang}")
    public ResponseEntity<?> updateHoaDon(@RequestBody UpdateHoaDonRequest request, @PathVariable Long maDonHang) {
        HoaDon hoaDon1 = hoaDonRepository.findById(maDonHang).orElse(null);

        if (hoaDon1 == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Mã hóa đơn không tồn tại"));
        }
        try {
            return ResponseEntity.ok(hoaDonService.updateOrder(request, maDonHang));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }
    }

    @GetMapping("/user/order/get-hoadon")
    public ResponseEntity<?> getHoaDon(Principal principal, @RequestParam("trangThai") Integer trangThai) {
        return ResponseEntity.ok(hoaDonService.getOrdersByUser(principal, trangThai));
    }

    @GetMapping("/user/order/get-hoadon/all")
    public ResponseEntity<?> getHoaDonAll(@Payload Principal principal) {
        List<HoaDonResponse> hoaDon = hoaDonService.getOrdersAllOk(principal);
        return ResponseEntity.ok(hoaDonService.getOrdersAllOk(principal));
    }


    @PutMapping("/admin/order/thaydoiTrangThai")
    @ResponseBody
    public ResponseEntity<?> capNhapTrangThaiHoaDonByAdmin(
            @RequestParam("maHD") String maHD, @RequestParam("trangThai") Integer trangThai) {
        UpdateStatus status = new UpdateStatus();
        status.setMaHoaDon(Long.parseLong(maHD));
        status.setTrangThai(trangThai);
        this.messagingTemplate.convertAndSend("/topic/orderStatus", status);
        return ResponseEntity.ok(hoaDonService.updateStatus(trangThai, Long.parseLong(maHD)));
    }


    @GetMapping("/user/order/get-hoadon/detail/{maHoaDon}")
    public ResponseEntity<?> getHoaDonDetail(@PathVariable Long maHoaDon) {
        return ResponseEntity.ok(hoaDonService.orderDetail(maHoaDon));
    }

    @GetMapping("/user/order/get-hoadon/{maHoaDon}")
    public ResponseEntity<?> getHoaDonAll(@PathVariable Long maHoaDon) {
        return ResponseEntity.ok(hoaDonService.getOrdersDetail(maHoaDon));
    }

    @PutMapping("/user/order/update-quantity")
    public ResponseEntity<?> updateQuantity(
            @RequestParam("SPCTId") String SPCTId,
            @RequestParam("soLuong") String soLuong) {
        HoaDonChiTiet hdct = hoaDonService.updateQuantity(Long.parseLong(SPCTId), Integer.parseInt(soLuong));
        if (hdct == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Sản phẩm không có trong hóa đơn"));
        }
        return ResponseEntity.ok(hdct);
    }

    @PutMapping("/user/order/huy-hoa-don")
    public ResponseEntity<?> huyHoaDon(
            @RequestParam("maHD") String maHD) {
        return ResponseEntity.ok(hoaDonService.huyHoaDon(Long.parseLong(maHD)));
    }

    @PostMapping("/guest/order/place")
    public ResponseEntity<?> taoHoaDonSession(@RequestBody NguoiDungSessionRequest request, HttpSession session) {
        Map<String, Integer> sessionCart = gioHangCTSessionService.getSessionCart(session);
        try {
            if (sessionCart == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Bạn chưa chọn sản phẩm nào"));
            }
            return ResponseEntity.ok(hoaDonService.hoaDonSession(session, request));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @DeleteMapping("/user/order/delete")
    public ResponseEntity<?> xoaHoaDon(
            @RequestParam("maHoaDonCT") Long maHDCT) {
        hoaDonService.deleteHDCT(maHDCT);
        return ResponseEntity.ok(new MessageResponse("Xóa sản phẩm trong hóa đơn thành công"));
    }
    //TODO: Update hóa đơn theo trạng thái là 0: User update đc
    //TODO: Update hóa đơn theo trạng thái là 1: ADMIN update đc
    //TODO: Update số lượng sản phẩm, địa chỉ,...
    //TODO: Làm hủy hóa hóa chuyển thành trạng thái 5

    @PostMapping("/user/order/update-so-luong")
    public ResponseEntity<?> updateSoLuong(@RequestParam Integer soLuong,
                                           @RequestParam Long maHoaDonCT,
                                           @RequestParam Long maHoaDon) {
        try {
            return ResponseEntity.ok(hoaDonService.suaSoLuongVaoHoaDon(maHoaDonCT, soLuong, maHoaDon));

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }
    }

    @PostMapping("/guest/order/thanh-toan")
    public ResponseEntity<?> thanhToanGuest(@RequestBody HoaDonRequest request, @RequestParam List<Long> ma) {
        try {
            NewOrder newOrder = new NewOrder();
            newOrder.setMa(ma);
            newOrder.setRequest(request);
            this.messagingTemplate.convertAndSend("/topic/orderStatus", newOrder);
            return ResponseEntity.ok(hoaDonService.thanhToanGuest(request, ma));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }

    }

    @PutMapping("/admin/order/update-status/{maDonHang}")
    public ResponseEntity<?> updateStatus(@RequestBody HoaDonRequest request, @PathVariable Long maDonHang) {
        HoaDon hoaDon1 = hoaDonRepository.findById(maDonHang).orElse(null);

        if (hoaDon1 == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Mã hóa đơn không tồn tại"));
        }
        return ResponseEntity.ok(hoaDonService.statusHoaDon(request, maDonHang));
    }

    @GetMapping("/admin/order/all")
    public ResponseEntity<?> getAllOrder(Pageable pageable) {
        return ResponseEntity.ok(hoaDonService.findAllHd(pageable));
    }

    @GetMapping("/admin/order/getAll")
    public ResponseEntity<?> getAllOrderByAdmin(Pageable pageable, @RequestParam("trangThai") Integer trangThai) {
        return ResponseEntity.ok(hoaDonService.getAllOrderByAdmin(pageable, trangThai));
    }

    @GetMapping("/admin/order/get-hoadon/detail/{maHoaDon}")
    public ResponseEntity<?> getHoaDonDetailAd(@PathVariable Long maHoaDon) {
        return ResponseEntity.ok(hoaDonService.orderDetail(maHoaDon));
    }

    @GetMapping("/admin/order/get-hoadon/{maHoaDon}")
    public ResponseEntity<?> getHoaDonAllAd(@PathVariable Long maHoaDon) {
        return ResponseEntity.ok(hoaDonService.getOrdersDetail(maHoaDon));
    }

    @PutMapping("/admin/order/huy-hoa-don")
    public ResponseEntity<?> huyHoaDonByAdmin(
            @RequestParam("maHD") String maHD) {
        return ResponseEntity.ok(hoaDonService.huyHoaDon(Long.parseLong(maHD)));
    }

    @GetMapping("/admin/order/search")
    public ResponseEntity<?> searchHoaDon(Pageable pageable,@RequestParam String thuocTinh,@RequestParam String value,Integer trangThai){
        return ResponseEntity.ok(hoaDonService.findHoaDon(pageable,thuocTinh,value,trangThai));
    }


    @PutMapping("/admin/order/thanhToan")
    public ResponseEntity<?> capNhapThanhToanHoaDonByAdmin(
            @RequestParam("maHD") String maHD, @RequestParam("thanhToan") Integer thanhToan) {
        return ResponseEntity.ok(hoaDonService.updatePaid(thanhToan, Long.parseLong(maHD)));
    }

    @PostMapping("/user/order/them-san-pham-vao-hoa-don")
    public ResponseEntity<?> themSanPhamVaoHoaDon(@RequestParam Long maSPCT,
                                           @RequestParam int soLuong,
                                           @RequestParam Long maHoaDon) {
        try {
            return ResponseEntity.ok(hoaDonService.themSanPhamVaoHoaDon(maSPCT, soLuong, maHoaDon));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }
    }

    @PostMapping("/staff/order")
    public ResponseEntity<?> taoHoaDonTaiQuay(@RequestBody HoaDonRequest request) {
        return ResponseEntity.ok(hoaDonService.taoHoaDonTaiQuay(request));
    }

    @GetMapping("/staff/get-all")
    public ResponseEntity<?> hienHoaDonTaiQuay() {
        return ResponseEntity.ok(hoaDonService.getAllHoaDonTaiQuay());
    }

    @GetMapping("/staff/order/get-hoadon")
    public ResponseEntity<?> getSanPham(@RequestParam Long maHoaDon) {
        return ResponseEntity.ok(hoaDonService.getOrdersDetai(maHoaDon));
    }

    @PostMapping("/staff/order/them-san-pham-vao-hoa-don")
    public ResponseEntity<?> themSanPhamVaoHoaDonTaiQuay(@RequestParam Long maSPCT,
                                                  @RequestParam int soLuong,
                                                  @RequestParam Long maHoaDon) {
        try {
            return ResponseEntity.ok(hoaDonService.themSanPhamVaoHoaDon(maSPCT, soLuong, maHoaDon));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }
    }

    @PostMapping("/staff/order/update-so-luong")
    public ResponseEntity<?> updateSoLuongHoaDonTaiQuay(@RequestParam Integer soLuong,
                                           @RequestParam Long maHoaDonCT,
                                           @RequestParam Long maHoaDon) {
        try {
            return ResponseEntity.ok(hoaDonService.suaSoLuongVaoHoaDon(maHoaDonCT, soLuong, maHoaDon));

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }
    }


    @PutMapping("/admin/order/update/{maDonHang}")
    public ResponseEntity<?> updateHoaDonByAdmin(@RequestBody UpdateHoaDonRequest request, @PathVariable Long maDonHang) {
        HoaDon hoaDon1 = hoaDonRepository.findById(maDonHang).orElse(null);

        if (hoaDon1 == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Mã hóa đơn không tồn tại"));
        }
        try {
            return ResponseEntity.ok(hoaDonService.updateOrder(request, maDonHang));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }
    }


    @PostMapping("/admin/order/add-productHd")
    public ResponseEntity<?> themSanPhamHDByAdmin(@RequestParam Long maSPCT,
    @RequestParam int soLuong,
    @RequestParam Long maHoaDon) {
        try {
            return ResponseEntity.ok(hoaDonService.themSanPhamVaoHoaDon(maSPCT, soLuong, maHoaDon));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }
    }

    @DeleteMapping("/admin/order/delete")
    public ResponseEntity<?> xoaSPByAdmin(
            @RequestParam("maHoaDonCT") Long maHDCT) {
        hoaDonService.deleteHDCT(maHDCT);
        return ResponseEntity.ok(new MessageResponse("Xóa sản phẩm trong hóa đơn thành công"));
    }
}
