package com.example.websonserver.api;

import com.example.websonserver.dto.request.HoaDonRequest;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.dto.response.ThanhToanRes;
import com.example.websonserver.entity.GioHang;
import com.example.websonserver.entity.GioHangChiTiet;
import com.example.websonserver.entity.HoaDon;
import com.example.websonserver.jwt.JwtTokenProvider;
import com.example.websonserver.repository.GioHangRepository;
import com.example.websonserver.repository.HoaDonRepository;
import com.example.websonserver.repository.PhuongThucThanhToanRepository;
import com.example.websonserver.repository.VoucherRepository;
import com.example.websonserver.service.HoaDonService;
import com.example.websonserver.service.UserDetailService;
import com.example.websonserver.service.serviceIpml.VnPayServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class HoaDonApi {
    @Autowired
    private HoaDonService hoaDonService;
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

    //TODO: Update hóa đơn theo trạng thái là 0: User update đc
    //TODO: Update hóa đơn theo trạng thái là 1: ADMIN update đc
    //TODO: Update số lượng sản phẩm, địa chỉ,...
    //TODO: Làm hủy hóa hóa chuyển thành trạng thái 5


}
