package com.example.websonserver.api;

import com.example.websonserver.dto.request.VoucherRequest;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.service.serviceIpml.VoucherServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@CrossOrigin("*")
@RequestMapping("/api")
@RestController
public class VoucherApi {

    @Autowired
    VoucherServiceImpl voucherService;

    @GetMapping("/admin/voucher")
    public ResponseEntity<?> getAllPage(){
        return ResponseEntity.ok(voucherService.getAllVoucher());
    }

    @GetMapping("/user/voucher")
    public ResponseEntity<?> getAllVoucher(){
        return ResponseEntity.ok(voucherService.getAllVoucher());
    }

    @PostMapping("/admin/voucher/add")
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
    @PutMapping("/admin/voucher/update/{ma}")
    public ResponseEntity<?> update(@Valid @RequestBody VoucherRequest voucherRequest, @PathVariable Long ma, BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        LocalDateTime now = LocalDateTime.now();
        if (voucherRequest.getThoiGianBatDau() != null && voucherRequest.getThoiGianBatDau().isAfter(now)) {
            voucherRequest.setTrangThai(1);
        } else {
            voucherRequest.setTrangThai(0);
        }
        if (voucherService.update(ma, voucherRequest) != null) {
                return ResponseEntity.ok("Sửa thành công");
            } else {
                return ResponseEntity.ok("Sửa thất bại");
            }
    }

    @DeleteMapping("/admin/voucher/delete/{ma}")
    public ResponseEntity<?> delete(@PathVariable Long ma){
        voucherService.deleteVoucher(ma);
            return ResponseEntity.ok(new MessageResponse("Xóa thành công"));

    }

}
