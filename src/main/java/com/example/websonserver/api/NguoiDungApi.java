package com.example.websonserver.api;

import com.example.websonserver.dto.request.NguoiDungRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.service.serviceIpml.NguoiDungServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class NguoiDungApi {
    @Autowired
    private NguoiDungServiceImpl nguoiDungService;
    @GetMapping("/admin/nguoi-dung")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(nguoiDungService.getAll());
    }
    @GetMapping("/admin/nguoi-dung/{ma}")
    public ResponseEntity<?> getById(@PathVariable String ma) {
        return ResponseEntity.ok(nguoiDungService.findById(ma));
    }
    @PostMapping("/admin/nguoi-dung/add")
    public ResponseEntity<?> save(@Valid @RequestBody NguoiDungRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if (nguoiDungService.existByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username đã tồn tại"));
        }
        if (nguoiDungService.existByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email đã tồn tại"));
        }
        return ResponseEntity.ok(nguoiDungService.create(request));
    }

    @PutMapping("/admin/nguoi-dung/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody NguoiDungRequest request, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(nguoiDungService.update(request, ma));
    }

    @DeleteMapping("/admin/nguoi-dung/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        nguoiDungService.delete(ma);
        return ResponseEntity.ok("oke nha");
    }

    @GetMapping("/admin/nguoi-dung/timKiem/{keyword}")
    public ResponseEntity<?> searchByKeyword(@PathVariable String keyword) {
        return ResponseEntity.ok(nguoiDungService.searchByKeyword(keyword));
    }

    @PutMapping("/admin/nguoi-dung/sua/{ma}")
    public ResponseEntity updateStatus(@RequestBody UpdateTrangThai trangThai, @PathVariable Long ma) {
        return ResponseEntity.ok(nguoiDungService.updateStatus(trangThai, ma));
    }

    @GetMapping("/staff/nguoi-dung/khach-hang")
    public ResponseEntity<?> getKhachHang() {
        return ResponseEntity.ok(nguoiDungService.getKhachHang());
    }
}
