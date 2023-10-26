package com.example.websonserver.api;

import com.example.websonserver.dto.request.AnhSanPhamRequest;
import com.example.websonserver.dto.response.AnhSanPhamResponse;
import com.example.websonserver.entity.AnhSanPham;
import com.example.websonserver.entity.SanPhamChiTiet;
import com.example.websonserver.service.serviceIpml.AnhSanPhamServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/anh")
public class AnhSanPhamApi {
    @Autowired
    private AnhSanPhamServiceImpl anhSanPhamService;

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(anhSanPhamService.getAll(pageable).getContent());
    }

    @GetMapping("/tat-ca")
    public ResponseEntity<?> getAllAnh(Pageable pageable) {
        return ResponseEntity.ok(anhSanPhamService.getAllAnh(pageable).getContent());
    }

    @GetMapping("/{ma}")
    public ResponseEntity<?> getById(@PathVariable String ma) {
        return ResponseEntity.ok(anhSanPhamService.findById(ma));
    }

    @PostMapping("/add")
    public ResponseEntity<?> save(@Valid @RequestBody AnhSanPhamRequest anhSP, BindingResult result, @RequestParam("file") MultipartFile data) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(anhSanPhamService.create(anhSP, data));
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

    @DeleteMapping("/delete-anh/{ma}")
    public ResponseEntity<?> deleteAnh(@PathVariable Long ma) {
        anhSanPhamService.deleteAnh(ma);
        return ResponseEntity.ok("Xóa ảnh ok");
    }

    @GetMapping("/sanPham")
    public ResponseEntity<?> getImageByProductID(@RequestParam("maSanPham") Long id) {
        return ResponseEntity.ok(anhSanPhamService.getImagesBySanPhamChiTiet(id));
    }

    @GetMapping("/images")
    public List<AnhSanPhamResponse> getAllImages() {
        List<AnhSanPham> images = anhSanPhamService.getAllAnh();
        List<AnhSanPhamResponse> imageDTOs = new ArrayList<>();

        for (AnhSanPham image : images) {
            AnhSanPhamResponse imageDTO = new AnhSanPhamResponse();
            imageDTO.setMaAnh(image.getMaAnh());
            imageDTO.setAnh(String.valueOf(image.getAnh()));
            imageDTOs.add(imageDTO);
        }

        return imageDTOs;
    }


    @GetMapping("/list-images")
    public ResponseEntity<List<Long>> listImages() {
        List<Long> imageIds = anhSanPhamService.getListOfImageIds();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageIds);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        anhSanPhamService.uploadAnh(file);
        return ResponseEntity.ok("Image uploaded with ID: ");
    }
}
