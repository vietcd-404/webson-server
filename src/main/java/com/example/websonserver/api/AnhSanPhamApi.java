package com.example.websonserver.api;

import com.example.websonserver.dto.request.AnhSanPhamRequest;
import com.example.websonserver.entity.AnhSanPham;
import com.example.websonserver.service.serviceIpml.AnhSanPhamServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/anhSanPham")
public class AnhSanPhamApi {
    @Autowired
    private AnhSanPhamServiceImpl anhSanPhamService;
    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(anhSanPhamService.getAll(pageable).getContent());
    }
    @GetMapping("/{ma}")
    public ResponseEntity<?> getById(@PathVariable String ma) {
        return ResponseEntity.ok(anhSanPhamService.findById(ma));
    }
    @PostMapping("/add")
    public ResponseEntity<?> save(@Valid @RequestBody AnhSanPhamRequest anhSP, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(anhSanPhamService.create(anhSP));
    }

    @PutMapping("/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody AnhSanPhamRequest anhSP, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(anhSanPhamService.update(anhSP, ma));
    }

    @DeleteMapping("/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        anhSanPhamService.delete(ma);
        return ResponseEntity.ok("oke nha");
    }

    @GetMapping("/sanPham")
    public ResponseEntity<?> getImageByProductID(@RequestParam("maSanPham") Long id) {
        return ResponseEntity.ok(anhSanPhamService.getImagesBySanPhamChiTiet(id));
    }
}
