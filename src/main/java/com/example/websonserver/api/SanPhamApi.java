package com.example.websonserver.api;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.SanPhamRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.entity.SanPham;
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
@RequestMapping("/api/staff/san-pham")
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
            return ResponseEntity.badRequest().body(new MessageResponse(result.getFieldError().getDefaultMessage()));
        }
        if (sanPhamServiceImpl.existsByTenSanPham(sanPham.getTenSanPham().trim())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Sản phẩm đã tồn tại"));
        }
        return ResponseEntity.ok(sanPhamServiceImpl.create(sanPham));
    }

    @PutMapping("/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody SanPhamRequest sanPham, @PathVariable Long ma, BindingResult result) {
        SanPham sanPham1 = sanPhamServiceImpl.getById(ma);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse(result.getFieldError().getDefaultMessage()));
        }
        if(sanPham1.getTenSanPham().equals(sanPham.getTenSanPham().trim())){
            return ResponseEntity.ok(sanPhamServiceImpl.update(sanPham, ma));
        }
        if ( sanPhamServiceImpl.existsByTenSanPham(sanPham.getTenSanPham().trim())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Sản phẩm đã tồn tại"));
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

    @PutMapping("/sua/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateTrangThai trangThai, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(sanPhamServiceImpl.updateStatus(trangThai, ma));
    }

    @GetMapping("/load-sp")
    public ResponseEntity<?> loadSanPham() {
        return ResponseEntity.ok(sanPhamServiceImpl.fillComboSpctBySanPham());
    }
}

