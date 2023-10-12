package com.example.websonserver.api;

import com.example.websonserver.dto.request.NguoiDungRequest;
import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.service.serviceIpml.NguoiDungServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/nguoiDung")
public class NguoiDungApi {
    @Autowired
    private NguoiDungServiceImpl nguoiDungService;
    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(nguoiDungService.getAll(pageable).getContent());
    }
    @GetMapping("/{ma}")
    public ResponseEntity<?> getById(@PathVariable String ma) {
        return ResponseEntity.ok(nguoiDungService.findById(ma));
    }
    @PostMapping("/add")
    public ResponseEntity<?> save(@Valid @RequestBody NguoiDungRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(nguoiDungService.create(request));
    }

    @PutMapping("/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody NguoiDungRequest request, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(nguoiDungService.update(request, ma));
    }

    @DeleteMapping("/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        nguoiDungService.delete(ma);
        return ResponseEntity.ok("oke nha");
    }

    @GetMapping("/timKiem/{keyword}")
    public ResponseEntity<?> searchByKeyword(@PathVariable String keyword) {
        return ResponseEntity.ok(nguoiDungService.searchByKeyword(keyword));
    }
}
