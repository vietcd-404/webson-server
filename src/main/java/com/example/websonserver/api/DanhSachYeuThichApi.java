package com.example.websonserver.api;

import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.entity.DanhSachYeuThich;
import com.example.websonserver.service.DanhSachYeuThichService;
import com.example.websonserver.service.serviceIpml.DanhSachYeuThichServiceImpl;
import com.example.websonserver.service.serviceIpml.SanPhamChiTietServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user/yeu-thich")
public class DanhSachYeuThichApi {
    @Autowired
    DanhSachYeuThichServiceImpl danhSachYeuThichService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(Pageable pageable){
        return ResponseEntity.ok(danhSachYeuThichService.getAllList(pageable));
    }

    @PostMapping("/add")
    public  ResponseEntity<?> addDanhsach( @RequestParam("SPCTId") Long maSPCT){
        DanhSachYeuThich danhSachYeuThich = danhSachYeuThichService.findByUser(maSPCT);
        if (danhSachYeuThich!=null){
            return ResponseEntity.badRequest().body(new MessageResponse("Sản phẩm đã tồn tại trong danh sách"));
        }
        return  ResponseEntity.ok(danhSachYeuThichService.addProduct(maSPCT));
    }

    @DeleteMapping("/delete")
    public  ResponseEntity<?> xoaDanhsach( @RequestParam("SPCTId") Long maSPCT){
        danhSachYeuThichService.deleteProductFromlist(maSPCT);
        return ResponseEntity.ok("Đã thực hiện xóa");
    }
}
