package com.example.websonserver.api;

import com.example.websonserver.entity.GioHangChiTiet;
import com.example.websonserver.service.GioHangChiTietService;
import com.example.websonserver.service.UserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user/gio-hang-chi-tiet")
public class GioHangChiTietApi {
    @Autowired
    private GioHangChiTietService gioHangChiTietService;

    @Autowired
    private UserDetailService userDetailService;
    @PostMapping("/add")
    public ResponseEntity<?> addProductToCart(
            @RequestParam("SPCTId") String SPCTId,
            @RequestParam("soLuong") String soLuong) {
        try {
            GioHangChiTiet ghct = gioHangChiTietService.addProductToCart(SPCTId, soLuong);
            return ResponseEntity.ok(ghct);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }
    @PutMapping("/update-product-quantity")
    public ResponseEntity<?> updateProductQuantity(
            @RequestParam("SPCTId") String SPCTId,
            @RequestParam("soLuong") String soLuong) {
        try {
            GioHangChiTiet ghct = gioHangChiTietService.updateProductQuantityInCart(SPCTId, soLuong);
            return ResponseEntity.ok(ghct);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(Pageable pageable){
        return ResponseEntity.ok(gioHangChiTietService.getAllCarts(pageable).getContent());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("SPCTId") String SPCTId) {
        gioHangChiTietService.deleteProductFromCart(Long.valueOf(SPCTId));
        return ResponseEntity.ok("oke nha");
    }
}
