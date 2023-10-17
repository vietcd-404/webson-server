package com.example.websonserver.api;

import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.service.AnhSanPhamService;
import com.example.websonserver.service.serviceIpml.SanPhamChiTietServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/san-pham-chi-tiet")
public class SanPhamChiTietApi {
    @Autowired
    private SanPhamChiTietServiceImpl sanPhamChiTietService;


    @Autowired
    private AnhSanPhamService anhSanPhamService;

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(sanPhamChiTietService.getAll(pageable).getContent());
    }

    @GetMapping("/all")
    public ResponseEntity<List<SanPhamChiTietResponse>> getAllSanPhamChiTietWithImages() {
        List<SanPhamChiTietResponse> sanPhamChiTietDtos = sanPhamChiTietService.getAllCT();
        return new ResponseEntity<>(sanPhamChiTietDtos, HttpStatus.OK);
    }


    @GetMapping("/{ma}")
    public ResponseEntity<?> getById(@PathVariable String ma) {
        return ResponseEntity.ok(sanPhamChiTietService.findById(ma));
    }

    @PostMapping("/add")
    public ResponseEntity<?> save(@Valid @RequestBody SanPhamChiTietRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(sanPhamChiTietService.create(request));
    }

    @PutMapping("/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody SanPhamChiTietRequest request, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(sanPhamChiTietService.update(request, ma));
    }

    @DeleteMapping("/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        sanPhamChiTietService.delete(ma);
        return ResponseEntity.ok("oke nha");
    }

    @GetMapping("/{maSanPhamCT}/images")
    public ResponseEntity<List<String>> getImages(@PathVariable Long maSanPhamCT) {
        List<String> imageUrls = anhSanPhamService.getImagesBySanPhamChiTiet(maSanPhamCT);
        return new ResponseEntity<>(imageUrls, HttpStatus.OK);
    }
}
