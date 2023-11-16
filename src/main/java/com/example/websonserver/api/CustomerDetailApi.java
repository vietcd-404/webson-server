package com.example.websonserver.api;

import com.example.websonserver.dto.request.MatKhauNguoiDungRequest;
import com.example.websonserver.dto.request.NguoiDungRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.service.serviceIpml.NguoiDungServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/guest")
public class CustomerDetailApi {
    @Autowired
    private NguoiDungServiceImpl nguoiDungService;

    private final PasswordEncoder passwordEncoder;

    public CustomerDetailApi(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/cap-nhap/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody NguoiDungRequest nguoiDungRequest, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(nguoiDungService.update(nguoiDungRequest, ma));
    }

    @PutMapping("/sua/{ma}")
    public ResponseEntity updateStatus(@RequestBody UpdateTrangThai trangThai, @PathVariable Long ma) {
        trangThai.setTrangThai(0);
        return ResponseEntity.ok(nguoiDungService.updateStatus(trangThai, ma));
    }

    @PutMapping("/cap-nhap-pass/{ma}")
    public ResponseEntity<?> updatePassWord(@Valid @RequestBody MatKhauNguoiDungRequest nguoiDungRequest, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        NguoiDung  nguoiDung = nguoiDungService.findById(ma);
        if (nguoiDungRequest.getOldpassword() == null || ("").equals(nguoiDungRequest.getOldpassword()) ||
                nguoiDungRequest.getNewpass() == null || ("").equals(nguoiDungRequest.getNewpass()) ||
                nguoiDungRequest.getRepass() == null || ("").equals(nguoiDungRequest.getRepass())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Không được bỏ trống mật khẩu"));
        }

        // Kiểm tra xem mật khẩu cũ có trùng khớp với mật khẩu trong cơ sở dữ liệu hay không
        if (!passwordEncoder.matches(nguoiDungRequest.getOldpassword(), nguoiDung.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Sai mật khẩu cũ"));
        }

        // Kiểm tra xem mật khẩu mới và nhắc lại mật khẩu có trùng khớp nhau hay không
        if (!nguoiDungRequest.getNewpass().equals(nguoiDungRequest.getRepass())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Sai nhắc lại mật khẩu"));
        }

        return ResponseEntity.ok(nguoiDungService.changePass(nguoiDungRequest, ma));
    }
}
