package com.example.websonserver.api;

import com.example.websonserver.dto.request.DiaChiRequest;
import com.example.websonserver.dto.request.GioHangRequest;
import com.example.websonserver.service.GioHangService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/gio-hang")
public class GioHangApi {
    @Autowired
    private GioHangService gioHangService;
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(gioHangService.getAll(pageable).getContent());
    }
    @PostMapping("/add")
    public ResponseEntity<?> save(@Valid @RequestBody GioHangRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(gioHangService.create(request));
    }
    @PutMapping("/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody GioHangRequest request, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(gioHangService.update(request, ma));
    }

    @DeleteMapping("/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        gioHangService.delete(ma);
        return ResponseEntity.ok("oke nha");
    }


}
