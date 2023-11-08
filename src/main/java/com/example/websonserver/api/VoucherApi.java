package com.example.websonserver.api;

import com.example.websonserver.dto.request.VoucherRequest;
import com.example.websonserver.service.serviceIpml.VoucherServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/voucher")
public class VoucherApi {

    @Autowired
    VoucherServiceImpl voucherService;

    @GetMapping
    public ResponseEntity<?> getAllPage(Pageable pageable){
        return ResponseEntity.ok(voucherService.getAllVoucher(pageable).getContent());
    }

    @PostMapping("/add")
    public ResponseEntity<?> getAllPage(@Valid @RequestBody VoucherRequest voucherRequest, BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if(voucherService.saveVoucher(voucherRequest)!=null){
            return ResponseEntity.ok("Thêm thành công");
        }else{
            return ResponseEntity.ok("Thêm thất bại");
        }
    }
    @PutMapping("/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody VoucherRequest voucherRequest, @PathVariable Long ma, BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if(voucherService.update(ma,voucherRequest)!=null){
            return ResponseEntity.ok("Sửa thành công");
        }else{
            return ResponseEntity.ok("Sửa thất bại");
        }
    }

    @PutMapping("/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma){
        if(voucherService.deleteVoucher(ma)!=0){
            return ResponseEntity.ok("Xóa thành công");
        }else{
            return ResponseEntity.ok("Xóa thất bại");
        }
    }

}
