package com.example.websonserver.api;

import com.example.websonserver.dto.response.GioHangChiTietResponse;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.service.GioHangCTSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/guest/gio-hang")
@SessionAttributes("sessionCart")
public class GioHangChiTietSessionApi {
    @Autowired
    private final GioHangCTSessionService gioHangCTSessionService;


    public GioHangChiTietSessionApi(GioHangCTSessionService gioHangCTSessionService) {
        this.gioHangCTSessionService = gioHangCTSessionService;
    }

    @GetMapping("/cart")
    public ResponseEntity<?> viewCart(Model model, HttpSession session) {
        Map<String, Integer> sessionCart = gioHangCTSessionService.getSessionCart(session);
        if (sessionCart == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Giỏ hàng trống"));
        }
        model.addAttribute("sessionCart", sessionCart);
        List<GioHangChiTietResponse> sessionCartDTO = gioHangCTSessionService.getSessionCartDTO(session);
        return ResponseEntity.ok(sessionCartDTO);
    }

    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(@RequestParam String SPCTId, @RequestParam int quantity, HttpSession session) {
        try {
            boolean addToCartSuccess = gioHangCTSessionService.addToSessionCart(SPCTId, quantity, session);

            if (addToCartSuccess) {
                return ResponseEntity.ok("Thêm vào giỏ hàng thành công");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số lượng không hợp lệ hoặc sản phẩm không tồn tại");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi thêm vào giỏ hàng");
        }
    }

    @PutMapping("/updateQuantityCart")
    public String updateQuantityCart(@RequestParam String SPCTId, @RequestParam int newQuantity, HttpSession session) {
        gioHangCTSessionService.updateQuantityInSessionCart(session, SPCTId, newQuantity);
        return "redirect:/cart";
    }

    @PutMapping("/updateCart")
    public String updateCart(@RequestParam String oldSPCTId, @RequestParam String newSPCTId, @RequestParam int quantity, HttpSession session) {
        gioHangCTSessionService.updateProductInSessionCart(session, oldSPCTId, newSPCTId, quantity);
        return "redirect:/cart";
    }

    @DeleteMapping("/removeFromCart")
    public String removeFromCart(@RequestParam String SPCTId, HttpSession session) {
        gioHangCTSessionService.removeFromSessionCart(SPCTId, session);
        return "redirect:/cart";
    }

    @PostMapping("/clearCart")
    public String clearCart(HttpSession session) {
        gioHangCTSessionService.clearSessionCart(session);
        return "redirect:/cart";
    }
}
