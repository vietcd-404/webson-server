package com.example.websonserver.api;

import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.dto.request.ThuocTinhRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.dto.response.SanPhamChiTietRes;
import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.entity.AnhSanPham;
import com.example.websonserver.entity.SanPhamChiTiet;
import com.example.websonserver.service.AnhSanPhamService;
import com.example.websonserver.service.serviceIpml.AnhSanPhamServiceImpl;
import com.example.websonserver.service.serviceIpml.SanPhamChiTietServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class SanPhamChiTietApi {
    @Autowired
    private SanPhamChiTietServiceImpl sanPhamChiTietService;


    @Autowired
    private AnhSanPhamServiceImpl anhSanPhamService;


    @GetMapping("/guest/san-pham/get-all")
    public ResponseEntity<?> getAllSanPham(
            @RequestParam(value = "maLoai", required = false) Long maLoai,
            @RequestParam(value = "maSanPham", required = false) Long maSanPham,
            @RequestParam(value = "maThuongHieu", required = false) Long maThuongHieu,
            @RequestParam(value = "maMau", required = false) Long maMau,
            @RequestParam(value = "tenSanPham", required = false) String tenSanPham,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "9") int size,
            @RequestParam(value = "giaCao", required = false) BigDecimal giaCao,
            @RequestParam(value = "giaThap", required = false) BigDecimal giaThap,
            @RequestParam(value = "giaGiamDan", required = false) Boolean giaGiamDan,
            @RequestParam(value = "giaTangDan", required = false) Boolean giaTangDan
    ) {
        ThuocTinhRequest request = new ThuocTinhRequest();
        request.setMaLoai(maLoai);
        request.setMaSanPham(maSanPham);
        request.setMaThuongHieu(maThuongHieu);
        request.setMaMau(maMau);
        request.setTenSanPham(tenSanPham);
        request.setGiaCao(giaCao);
        request.setGiaGiamDan(giaGiamDan);
        request.setGiaTangDan(giaTangDan);
        request.setGiaThap(giaThap);
        return ResponseEntity.ok(sanPhamChiTietService.getAllSanPhamUser(request,page,size));
    }

    @GetMapping("/guest/san-pham/{ma}")
    public ResponseEntity<?> getDetailById(@PathVariable String ma) {
        return ResponseEntity.ok(sanPhamChiTietService.findById(ma));
    }
    @GetMapping("/guest/san-pham-chi-tiet/{maSanPhamCT}/images")
    public ResponseEntity<List<AnhSanPham>> getImagesByGuest(@PathVariable Long maSanPhamCT) {
        List<AnhSanPham> imageUrls = anhSanPhamService.getImage(maSanPhamCT);
        return new ResponseEntity<>(imageUrls, HttpStatus.OK);
    }

    @GetMapping("/admin/san-pham-chi-tiet")
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(sanPhamChiTietService.getAll(pageable).getContent());
    }

    @GetMapping("/admin/san-pham-chi-tiet/all")
    public ResponseEntity<List<SanPhamChiTietResponse>> getAllSanPhamChiTietWithImages() {
        List<SanPhamChiTietResponse> sanPhamChiTietDtos = sanPhamChiTietService.getAllCT();
        return new ResponseEntity<>(sanPhamChiTietDtos, HttpStatus.OK);
    }


    @GetMapping("/admin/san-pham-chi-tiet/{ma}")
    public ResponseEntity<?> getById(@PathVariable String ma) {
        return ResponseEntity.ok(sanPhamChiTietService.findById(ma));
    }

    @PostMapping("/admin/san-pham-chi-tiet/add")
    public ResponseEntity<?> save(@Valid @RequestBody SanPhamChiTietRequest request, BindingResult result) {
        if (result.hasErrors()) {

            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(sanPhamChiTietService.createOne(request));

    }
    @PostMapping("/admin/san-pham-chi-tiet/{productId}/images")
    public ResponseEntity<String> addImagesToProductChiTiet(
            @PathVariable Long productId,
            @RequestBody List<Long> imageIds) {
        sanPhamChiTietService.addImagesToProductChiTiet(productId, imageIds);
        return ResponseEntity.ok("Đã thêm ảnh vào sản phẩm chi tiết thành công.");
    }

    @PostMapping("/admin/san-pham-chi-tiet/add-all")
    public ResponseEntity<?> saveAll(@Valid @RequestBody List<SanPhamChiTietRequest> listRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(sanPhamChiTietService.createList(listRequest));
    }

    @PutMapping("/admin/san-pham-chi-tiet/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody SanPhamChiTietRequest request, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(sanPhamChiTietService.update(request, ma));
    }

    @DeleteMapping("/admin/san-pham-chi-tiet/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        sanPhamChiTietService.delete(ma);
        return ResponseEntity.ok("oke nha");
    }

    @GetMapping("/admin/san-pham-chi-tiet/{maSanPhamCT}/images")
    public ResponseEntity<List<AnhSanPham>> getImages(@PathVariable Long maSanPhamCT) {
        List<AnhSanPham> imageUrls = anhSanPhamService.getImage(maSanPhamCT);
        return new ResponseEntity<>(imageUrls, HttpStatus.OK);
    }

    @PutMapping("/admin/san-pham-chi-tiet/sua/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateTrangThai request, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(sanPhamChiTietService.updateStatus(request, ma));
    }


}
