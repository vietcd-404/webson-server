package com.example.websonserver.api;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.SanPhamRequest;
import com.example.websonserver.service.serviceIpml.LoaiServiceIpml;
import com.example.websonserver.service.serviceIpml.SanPhamServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/san-pham")
public class SanPhamApi {
    @Autowired
    private SanPhamServiceImpl sanPhamServiceImpl;

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(sanPhamServiceImpl.getAll(pageable).getContent());
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveSanPham(@Valid @RequestBody SanPhamRequest sanPham, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(sanPhamServiceImpl.create(sanPham));
    }

    @PutMapping("/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody SanPhamRequest sanPham, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(sanPhamServiceImpl.update(sanPham, ma));
    }

    @DeleteMapping("/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        sanPhamServiceImpl.delete(ma);
        return ResponseEntity.ok("oke nha");
    }
    @GetMapping("/get-one/{tenSP}")
    public ResponseEntity<?> getOne(@PathVariable String tenSP) {
        return ResponseEntity.ok(sanPhamServiceImpl.findByTen(tenSP));
    }
}

