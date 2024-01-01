package com.example.websonserver.api;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.ThuongHieuRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.entity.SanPham;
import com.example.websonserver.entity.ThuongHieu;
import com.example.websonserver.service.serviceIpml.LoaiServiceIpml;
import com.example.websonserver.service.serviceIpml.ThuongHieuServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class ThuongHieuApi {
    @Autowired
    private ThuongHieuServiceImpl thuongHieuServiceImpl;

    @GetMapping("/staff/thuong-hieu")
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(thuongHieuServiceImpl.getAll(pageable).getContent());
    }

    @GetMapping("/guest/filter/thuong-hieu")
    public ResponseEntity<?> getAllFill(Pageable pageable) {
        return ResponseEntity.ok(thuongHieuServiceImpl.fillComboSpct());
    }

    @PostMapping("/staff/thuong-hieu/add")
    public ResponseEntity<?> saveLoai(@Valid @RequestBody ThuongHieuRequest thuongHieu, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse(result.getFieldError().getDefaultMessage()));
        }
        if (thuongHieuServiceImpl.existsByTenSanPham(thuongHieu.getTenThuongHieu().trim())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tên thương hiệu đã tồn tại"));
        }
        return ResponseEntity.ok(thuongHieuServiceImpl.create(thuongHieu));
    }

    @PutMapping("/staff/thuong-hieu/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody ThuongHieuRequest thuongHieu, @PathVariable Long ma, BindingResult result) {
        ThuongHieu thuongHieu1 = thuongHieuServiceImpl.getById(ma);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse(result.getFieldError().getDefaultMessage()));
        }
        if(thuongHieu1.getTenThuongHieu().equals(thuongHieu.getTenThuongHieu().trim())){
            return ResponseEntity.ok(thuongHieuServiceImpl.update(thuongHieu, ma));
        }
        if (thuongHieuServiceImpl.existsByTenSanPham(thuongHieu.getTenThuongHieu().trim())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tên thương hiệu đã tồn tại"));
        }
        return ResponseEntity.ok(thuongHieuServiceImpl.update(thuongHieu, ma));
    }

    @DeleteMapping("/staff/thuong-hieu/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        thuongHieuServiceImpl.delete(ma);
        return ResponseEntity.ok("oke nha");
    }

    @PutMapping("/staff/thuong-hieu/sua/{ma}")
    public ResponseEntity<?> updateStatus(@Valid @RequestBody UpdateTrangThai trangThai, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(thuongHieuServiceImpl.updateStatus(trangThai, ma));
    }

    @GetMapping("/staff/thuong-hieu/load-thuong-hieu")
    public ResponseEntity<?> loadThuongHieu() {
        return ResponseEntity.ok(thuongHieuServiceImpl.fillComboSpct());
    }
}