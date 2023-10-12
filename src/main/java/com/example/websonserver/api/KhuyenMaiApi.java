package com.example.websonserver.api;

import com.example.websonserver.dto.request.KhuyenMaiRequest;
import com.example.websonserver.service.serviceIpml.KhuyenMaiServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/khuyen-mai")
public class KhuyenMaiApi {

    @Autowired
    KhuyenMaiServiceImpl khuyenMaiService;

    @GetMapping
    public ResponseEntity<?> getAllPage(){
        return ResponseEntity.ok(khuyenMaiService.getAllKhuyenMai());
    }

    @PostMapping("/add")
    public ResponseEntity<?> getAllPage(@Valid @RequestBody KhuyenMaiRequest khuyenMaiRequest, BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if(khuyenMaiService.saveKhuyenMai(khuyenMaiRequest)!=null){
            return ResponseEntity.ok("Thêm thành công");
        }else{
            return ResponseEntity.ok("Thêm thất bại");
        }
    }
    @PutMapping("/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody KhuyenMaiRequest khuyenMaiRequest, @PathVariable Long ma, BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if(khuyenMaiService.update(ma,khuyenMaiRequest)!=null){
            return ResponseEntity.ok("Sửa thành công");
        }else{
            return ResponseEntity.ok("Sửa thất bại");
        }
    }

    @PutMapping("/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma){
        if(khuyenMaiService.deleteKhuyenMai(ma)!=0){
            return ResponseEntity.ok("Xóa thành công");
        }else{
            return ResponseEntity.ok("Xóa thất bại");
        }
    }

}
