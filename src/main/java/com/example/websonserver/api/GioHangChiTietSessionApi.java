package com.example.websonserver.api;

import com.example.websonserver.dto.response.GioHangChiTietResponse;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.service.GioHangCTSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth/gio-hang")
@SessionAttributes("sessionCart")
public class GioHangChiTietSessionApi {
    @Autowired
    private final GioHangCTSessionService gioHangCTSessionService;


    public GioHangChiTietSessionApi(GioHangCTSessionService gioHangCTSessionService) {
        this.gioHangCTSessionService = gioHangCTSessionService;
    }

    @GetMapping("/cart")
    public ResponseEntity<?> viewCart(Model model, HttpSession session) {
        Map<String, Integer> map = (Map<String, Integer>) session.getAttribute("sessionCart");
        if (map == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Giỏ hàng trống"));
        }
        Map<String, Integer> sessionCart = gioHangCTSessionService.getSessionCart(session);
        model.addAttribute("sessionCart", sessionCart);
        List<GioHangChiTietResponse> sessionCartDTO= gioHangCTSessionService.getSessionCartDTO(session);
        return ResponseEntity.ok(sessionCartDTO);
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam String SPCTId, @RequestParam int quantity, HttpSession session) {
        gioHangCTSessionService.addToSessionCart(SPCTId, quantity, session);
        return "redirect:/cart";
    }
    @PutMapping("/updateQuantityCart")
    public String updateQuantityCart(@RequestParam String SPCTId, @RequestParam int newQuantity, HttpSession session) {
        gioHangCTSessionService.updateQuantityInSessionCart(session, SPCTId, newQuantity);
        return "redirect:/cart";
    }
    @PutMapping("/updateCart")
    public String updateCart(@RequestParam String oldSPCTId,@RequestParam String newSPCTId, @RequestParam int quantity, HttpSession session) {
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
