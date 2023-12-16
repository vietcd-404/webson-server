package com.example.websonserver.api;

import com.example.websonserver.dto.request.LoaiResquest;
import com.example.websonserver.dto.request.MauSacRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.entity.MauSac;
import com.example.websonserver.entity.ThuongHieu;
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

    @GetMapping("/staff/mau-sac")
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(mauSacServiceImpl.getAll(pageable).getContent());
    }

    @GetMapping("/guest/filter/mau-sac")
    public ResponseEntity<?> getAllFill(Pageable pageable) {
        return ResponseEntity.ok(mauSacServiceImpl.fillComboSpctByNMau());
    }

    @PostMapping("/staff/mau-sac/add")
    public ResponseEntity<?> saveMauSac(@Valid @RequestBody MauSacRequest mauSac, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse(result.getFieldError().getDefaultMessage()));
        }
        if (mauSacServiceImpl.existsByTenSanPham(mauSac.getTenMau().trim())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tên màu đã tồn tại"));
        }
        return ResponseEntity.ok(mauSacServiceImpl.create(mauSac));
    }

    @PutMapping("/staff/mau-sac/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody MauSacRequest mauSac, @PathVariable Long ma, BindingResult result) {
        MauSac mauSac1 = mauSacServiceImpl.getById(ma);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse(result.getFieldError().getDefaultMessage()));
        }
        if(mauSac1.getTenMau().equals(mauSac.getTenMau().trim())){
            return ResponseEntity.ok(mauSacServiceImpl.update(mauSac, ma));
        }
        if (mauSacServiceImpl.existsByTenSanPham(mauSac.getTenMau().trim())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tên màu đã tồn tại"));
        }
        return ResponseEntity.ok(mauSacServiceImpl.update(mauSac, ma));
    }

    @DeleteMapping("/staff/mau-sac/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        mauSacServiceImpl.delete(ma);
        return ResponseEntity.ok("oke nha");
    }

    @PutMapping("/staff/mau-sac/sua/{ma}")
    public ResponseEntity<?> updateStatus(@Valid @RequestBody UpdateTrangThai trangThai, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(mauSacServiceImpl.updateStatus(trangThai, ma));
    }

    @GetMapping("/staff/mau-sac/load-mau")
    public ResponseEntity<?> loadMau() {
        return ResponseEntity.ok(mauSacServiceImpl.fillComboSpctByNMau());
    }
}
