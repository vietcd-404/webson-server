package com.example.websonserver.api;

import com.example.websonserver.dto.request.HoaDonRequest;
import com.example.websonserver.dto.request.NguoiDungSessionRequest;
import com.example.websonserver.dto.response.HoaDonResponse;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.dto.response.ThanhToanRes;
import com.example.websonserver.entity.GioHang;
import com.example.websonserver.entity.GioHangChiTiet;
import com.example.websonserver.entity.HoaDon;
import com.example.websonserver.entity.HoaDonChiTiet;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
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

    @PostMapping("/user/order/place/{maGioHang}")
    public ResponseEntity<?> placeOrder(@RequestBody HoaDonRequest hoaDon, @PathVariable Long maGioHang) {
        GioHang gioHang = gioHangRepository.findById(maGioHang).orElse(null);
        try {
            if (gioHang == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Giỏ hàng không tồn tại"));
            }
            return ResponseEntity.ok(hoaDonService.placeOrder(hoaDon, maGioHang));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
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

    @GetMapping("/user/order/get-hoadon")
    public ResponseEntity<?> getHoaDon(Principal principal, @RequestParam("trangThai") Integer trangThai) {
        return ResponseEntity.ok(hoaDonService.getOrdersByUser(principal, trangThai));
    }

    @GetMapping("/user/order/get-hoadon/all")
    public ResponseEntity<?> getHoaDonAll(Principal principal) {
        return ResponseEntity.ok(hoaDonService.getOrdersAllOk(principal));
    }

    @GetMapping("/user/order/get-hoadon/detail/{maHoaDon}")
    public ResponseEntity<?> getHoaDonDetail(@PathVariable Long maHoaDon) {
        return ResponseEntity.ok(hoaDonService.orderDetail(maHoaDon));
    }

    @GetMapping("/user/order/get-hoadon/{maHoaDon}")
    public ResponseEntity<?> getHoaDonAll(Principal principal,@PathVariable Long maHoaDon) {
        return ResponseEntity.ok(hoaDonService.getOrdersDetail(principal,maHoaDon));
    }

    @PutMapping("/user/order/update-quantity")
    public ResponseEntity<?> updateQuantity(
            Principal principal,
            @RequestParam("SPCTId") String SPCTId,
            @RequestParam("soLuong") String soLuong) {
        HoaDonChiTiet hdct = hoaDonService.updateQuantity(principal,Long.parseLong(SPCTId),Integer.parseInt(soLuong));
        if (hdct == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Sản phẩm không có trong hóa đơn"));
        }
        return ResponseEntity.ok(hdct);
    }

    @PutMapping("/user/order/huy-hoa-don")
    public ResponseEntity<?> huyHoaDon(
            @RequestParam("maHD") String maHD) {
        return ResponseEntity.ok(hoaDonService.HuyHoaDon(Long.parseLong(maHD)));
    }
    @PostMapping("/auth/order/place")
    public ResponseEntity<?> taoHoaDonSession(@RequestBody NguoiDungSessionRequest request, HttpSession session) {
        Map<String, Integer> sessionCart = gioHangCTSessionService.getSessionCart(session);
        try {
            if (sessionCart==null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Bạn chưa chọn sản phẩm nào"));
            }
            return ResponseEntity.ok(hoaDonService.hoaDonSession(session, request));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }
    //TODO: Update hóa đơn theo trạng thái là 0: User update đc
    //TODO: Update hóa đơn theo trạng thái là 1: ADMIN update đc
    //TODO: Update số lượng sản phẩm, địa chỉ,...
    //TODO: Làm hủy hóa hóa chuyển thành trạng thái 5


}
