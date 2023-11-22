package com.example.websonserver.api;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.MauSacRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.service.serviceIpml.LoaiServiceIpml;
import com.example.websonserver.service.serviceIpml.MauSacServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class MauSacApi {
    @Autowired
    private MauSacServiceImpl mauSacServiceImpl;

    @GetMapping("/admin/mau-sac")
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(mauSacServiceImpl.getAll(pageable).getContent());
    }

    @GetMapping("/guest/filter/mau-sac")
    public ResponseEntity<?> getAllFill(Pageable pageable) {
        return ResponseEntity.ok(mauSacServiceImpl.fillComboSpctByNMau());
    }

    @PostMapping("/admin/mau-sac/add")
    public ResponseEntity<?> saveMauSac(@Valid @RequestBody MauSacRequest mauSac, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(mauSacServiceImpl.create(mauSac));
    }

    @PutMapping("/admin/mau-sac/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody MauSacRequest mauSac, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(mauSacServiceImpl.update(mauSac, ma));
    }

    @DeleteMapping("/admin/mau-sac/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        mauSacServiceImpl.delete(ma);
        return ResponseEntity.ok("oke nha");
    }

    @PutMapping("/admin/mau-sac/sua/{ma}")
    public ResponseEntity<?> updateStatus(@Valid @RequestBody UpdateTrangThai trangThai, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(mauSacServiceImpl.updateStatus(trangThai, ma));
    }

    @GetMapping("/admin/mau-sac/load-mau")
    public ResponseEntity<?> loadMau() {
        return ResponseEntity.ok(mauSacServiceImpl.fillComboSpctByNMau());
    }
}
