package com.example.websonserver.api;

import com.example.websonserver.dto.request.PhuongThucThanhToanRequest;
import com.example.websonserver.dto.request.PhuongThucThanhToanRequest;
import com.example.websonserver.service.serviceIpml.PhuongThucThanhToanServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/phuong-thuc-thanh-toan")
public class PhuongThucThanhToanApi {

    @Autowired
    private PhuongThucThanhToanServiceImpl phuongThucThanhToanService;

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(phuongThucThanhToanService.getAll(pageable).getContent());
    }

    @PostMapping("/add")
    public ResponseEntity<?> save(@Valid @RequestBody PhuongThucThanhToanRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(phuongThucThanhToanService.create(request));
    }

    @PutMapping("/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody PhuongThucThanhToanRequest loai, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(phuongThucThanhToanService.update(loai, ma));
    }

    @DeleteMapping("/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        phuongThucThanhToanService.delete(ma);
        return ResponseEntity.ok("oke nha");
    }
}
