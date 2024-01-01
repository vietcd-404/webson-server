package com.example.websonserver.api;

import com.example.websonserver.dto.request.DiaChiRequest;
import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.service.serviceIpml.DiaChiServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user/dia-chi")
public class DiaChiApi {
    @Autowired
    private DiaChiServiceImpl diaChiService;

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(diaChiService.getAll(pageable).getContent());
    }
    @GetMapping("/{ma}")
    public ResponseEntity<?> getById(@PathVariable String ma) {
        return ResponseEntity.ok(diaChiService.findById(ma));
    }
    @PostMapping("/add")
    public ResponseEntity<?> save(@Valid @RequestBody DiaChiRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(diaChiService.create(request));
    }

    @PutMapping("/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody DiaChiRequest request, @PathVariable Long ma, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(diaChiService.update(request, ma));
    }

    @DeleteMapping("/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma) {
        diaChiService.delete(ma);
        return ResponseEntity.ok("Xóa thành công");
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> all(Principal principal) {
        return ResponseEntity.ok(diaChiService.getDiaChiTheoNguoiDung(principal));
    }
}
