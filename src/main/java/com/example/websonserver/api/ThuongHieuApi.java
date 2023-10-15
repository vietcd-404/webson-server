package com.example.websonserver.api;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.ThuongHieuRequest;
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
@RequestMapping("/api/admin/thuong-hieu")
public class ThuongHieuApi {
    @Autowired
    private ThuongHieuServiceImpl thuongHieuServiceImpl;

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(thuongHieuServiceImpl.getAll(pageable).getContent());
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveLoai(@Valid @RequestBody ThuongHieuRequest thuongHieu, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(thuongHieuServiceImpl.create(thuongHieu));
    }

    @PutMapping("/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody ThuongHieuRequest thuongHieu, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(thuongHieuServiceImpl.update(thuongHieu, ma));
    }

    @DeleteMapping("/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        thuongHieuServiceImpl.delete(ma);
        return ResponseEntity.ok("oke nha");
    }
}