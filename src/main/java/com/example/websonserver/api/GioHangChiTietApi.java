package com.example.websonserver.api;

import com.example.websonserver.dto.response.GioHangChiTietResponse;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.service.UserDetailService;
import com.example.websonserver.service.serviceIpml.GioHangChiTietServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class GioHangChiTietApi {
    @Autowired
    private GioHangChiTietServiceImpl gioHangChiTietService;

    @Autowired
    private UserDetailService userDetailService;
    @PostMapping("/user/gio-hang-chi-tiet/add")
    public ResponseEntity<?> addProductToCart(
            @RequestParam("SPCTId") String SPCTId,
            @RequestParam("soLuong") Integer soLuong) {
        try {
            GioHangChiTietResponse ghct = gioHangChiTietService.addProductToCart(SPCTId, soLuong);
            return ResponseEntity.ok(ghct);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }
    }

    @PostMapping("/staff/gio-hang-chi-tiet/add")
    public ResponseEntity<?> addSanPhamVaoGioHang(
            @RequestParam("SPCTId") String SPCTId,
            @RequestParam("soLuong") Integer soLuong) {
        try {
            GioHangChiTietResponse ghct = gioHangChiTietService.addProductToCart(SPCTId, soLuong);
            return ResponseEntity.ok(ghct);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }
    }
    @PutMapping("/staff/gio-hang-chi-tiet/update-product-quantity")
    public ResponseEntity<?> updateTaiQuay(
            @RequestParam("SPCTId") String SPCTId,
            @RequestParam("soLuong") String soLuong) {
        try {
            GioHangChiTietResponse ghct = gioHangChiTietService.updateProductQuantityInCart(SPCTId, soLuong);
            return ResponseEntity.ok(ghct);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }
    }

    @PutMapping("/user/gio-hang-chi-tiet/update-product-quantity")
    public ResponseEntity<?> updateProductQuantity(
            @RequestParam("SPCTId") String SPCTId,
            @RequestParam("soLuong") String soLuong) {
        try {
            GioHangChiTietResponse ghct = gioHangChiTietService.updateProductQuantityInCart(SPCTId, soLuong);
            return ResponseEntity.ok(ghct);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
        }
    }
    @GetMapping("/user/gio-hang-chi-tiet/getAll")
    public ResponseEntity<?> getAll(Pageable pageable){
        return ResponseEntity.ok(gioHangChiTietService.getAllCarts(pageable));
    }

    @GetMapping("/guest/getOne")
    public ResponseEntity<?> getByNGuoiDung(Pageable pageable,HttpSession session){
        return ResponseEntity.ok(gioHangChiTietService.getCart(pageable,session));
    }

    @DeleteMapping("/user/gio-hang-chi-tiet/delete")
    public ResponseEntity<?> delete(@RequestParam("SPCTId") String SPCTId) {
        gioHangChiTietService.deleteProductFromCart(Long.valueOf(SPCTId));
        return ResponseEntity.ok(new MessageResponse("Xóa giỏ hàng thành công"));
    }

    @GetMapping("/user/gio-hang-chi-tiet/all")
    public ResponseEntity<?> getGioHangUser(Principal principal) {
        return ResponseEntity.ok(gioHangChiTietService.gioHangUser(principal));
    }
    @GetMapping("/staff/gio-hang-chi-tiet/all")
    public ResponseEntity<?> getGioHangStaff(Principal principal) {
        return ResponseEntity.ok(gioHangChiTietService.gioHangUser(principal));
    }

    @DeleteMapping("/user/gio-hang-chi-tiet/delete-all")
    public ResponseEntity<?> deleteAll(Principal principal) {
        gioHangChiTietService.deleteAllGioHang(principal);
        return ResponseEntity.ok(new MessageResponse("Xóa tất cả giỏ hàng theo " + principal.getName()));
    }

    @DeleteMapping("/user/gio-hang-chi-tiet/delete-gio-hang")
    public ResponseEntity<?> delete(@RequestParam("maGioHangCT") Long ma) {
        gioHangChiTietService.deleteGioHang(ma);
        return ResponseEntity.ok(new MessageResponse("Xóa giỏ hàng thành công"));
    }

    @DeleteMapping("/staff/gio-hang-chi-tiet/delete-gio-hang")
    public ResponseEntity<?> deleteTaiQuay(@RequestParam("maGioHangCT") Long ma) {
        gioHangChiTietService.deleteGioHang(ma);
        return ResponseEntity.ok(new MessageResponse("Xóa giỏ hàng thành công"));
    }
}
