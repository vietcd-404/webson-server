package com.example.websonserver.api;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.service.serviceIpml.LoaiServiceIpml;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class LoaiApi {
    @Autowired
    private LoaiServiceIpml loaiServiceIpml;

    @GetMapping("/admin/loai")
    public ResponseEntity<?> getAll(Pageable pageable) {
            return ResponseEntity.ok(loaiServiceIpml.getAll(pageable).getContent());
    }

    @GetMapping("/guest/filter")
    public ResponseEntity<?> getAllFill(Pageable pageable) {
        return ResponseEntity.ok(loaiServiceIpml.fillComboSpct());
    }



    @PostMapping("/admin/loai/add")
    public ResponseEntity<?> saveLoai(@Valid @RequestBody LoaiResquest loai, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(loaiServiceIpml.create(loai));
    }

    @PutMapping("/admin/loai/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody LoaiResquest loai, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(loaiServiceIpml.update(loai, ma));
    }

    @DeleteMapping("/admin/loai/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        loaiServiceIpml.delete(ma);
        return ResponseEntity.ok("oke nha");
    }

    @PutMapping("/admin/loai/sua/{ma}")
    public ResponseEntity<?> updateStatus(@Valid @RequestBody UpdateTrangThai trangThai, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(loaiServiceIpml.updateStatusLoai(trangThai, ma));
    }

    @GetMapping("/admin/loai/load-loai")
    public ResponseEntity<?> loadAll() {
        return ResponseEntity.ok(loaiServiceIpml.fillComboSpct());
    }
}
