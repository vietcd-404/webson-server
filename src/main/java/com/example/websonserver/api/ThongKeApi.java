package com.example.websonserver.api;

import com.example.websonserver.dto.response.ThongKeResponse.ThongTinThongKe;
import com.example.websonserver.entity.HoaDonChiTiet;
import com.example.websonserver.repository.ThongKeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin")
public class ThongKeApi {
    @Autowired
    ThongKeRepository thongKeRepository;
    @GetMapping("/doanh-thu-theo-nam")
    public ResponseEntity<?> getDoanhThuTheoNam(@RequestParam Integer year){
        List<Object[]> lst = thongKeRepository.getDoanhThuTheoNam(year);
        List<ThongTinThongKe> list = new ArrayList<>();
        for (Object[] x : lst) {
            ThongTinThongKe  data = new ThongTinThongKe();
            data.setDoanhThu(x[0].toString());
            data.setSlDaBan(x[1].toString());
            list.add(data);
        }
        return ResponseEntity.ok(list);
    }
    @GetMapping("/doanh-thu-theo-thang")
    public ResponseEntity<?> getDoanhThuTheoThang(@RequestParam Integer month){
        List<Object[]> lst = thongKeRepository.getDoanhThuTheoThang(month);
        List<ThongTinThongKe> list = new ArrayList<>();
        for (Object[] x : lst) {
            ThongTinThongKe  data = new ThongTinThongKe();
            data.setDoanhThu(x[0].toString());
            data.setSlDaBan(x[1].toString());
            list.add(data);
        }
        return ResponseEntity.ok(list);
    }
    @GetMapping("/doanh-thu-theo-ngay")
    public ResponseEntity<?> getDoanhThuTheoNgay(@RequestParam Integer day){
        List<Object[]> lst = thongKeRepository.getDoanhThuTheoNgay(day);
        List<ThongTinThongKe> list = new ArrayList<>();
        for (Object[] x : lst) {
            ThongTinThongKe  data = new ThongTinThongKe();
            data.setDoanhThu(x[0].toString());
            data.setSlDaBan(x[1].toString());
            list.add(data);
        }
        return ResponseEntity.ok(list);
    }
    @GetMapping("/so-luong-hoa-don-theo-trang-thai-va-ngay")
    public ResponseEntity<?> getSoLuongHoaDonTheoTrangThaiVaNgay(@RequestParam Integer day,@RequestParam Integer status){
        List<Object[]> lst = thongKeRepository.getSoLuongHoaDonTheoTrangThaiVaNgay(day,status);
        Map<String,String> map = new HashMap<>();
        for (Object[] x : lst) {
            map.put("soLuong",x[0].toString());
        }
        return ResponseEntity.ok(map);
    }
    @GetMapping("/so-luong-hoa-don-theo-trang-thai-va-thang")
    public ResponseEntity<?> getSoLuongHoaDonTheoTrangThaivaThang(@RequestParam Integer month,@RequestParam Integer status){
        List<Object[]> lst = thongKeRepository.getSoLuongHoaDonTheoTrangThaiVaThang(month,status);
        Map<String,String> map = new HashMap<>();
        for (Object[] x : lst) {
            map.put("soLuong",x[0].toString());
        }
        return ResponseEntity.ok(map);
    }

    @GetMapping("/so-luong-hoa-don-theo-trang-thai-va-nam")
    public ResponseEntity<?> getSoLuongHoaDonTheoTrangThaivaNam(@RequestParam Integer year,@RequestParam Integer status){
        List<Object[]> lst = thongKeRepository.getSoLuongHoaDonTheoTrangThaiVaNam(year,status);
        Map<String,String> map = new HashMap<>();
        for (Object[] x : lst) {
            map.put("soLuong",x[0].toString());
        }
        return ResponseEntity.ok(map);
    }

    @GetMapping("/doanh-thu-theo-quy-1")
    public ResponseEntity<?> getDoanhThuTheoQuy1(){
        List<Object[]> lst = thongKeRepository.getDoanhThuTheoQuy1();
        List<ThongTinThongKe> list = new ArrayList<>();
        for (Object[] x : lst) {
            ThongTinThongKe  data = new ThongTinThongKe();
            data.setDoanhThu(x[0].toString());
            data.setSlDaBan(x[1].toString());
            list.add(data);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/doanh-thu-theo-quy-2")
    public ResponseEntity<?> getDoanhThuTheoQuy2(){
        List<Object[]> lst = thongKeRepository.getDoanhThuTheoQuy2();
        List<ThongTinThongKe> list = new ArrayList<>();
        for (Object[] x : lst) {
            ThongTinThongKe  data = new ThongTinThongKe();
            data.setDoanhThu(x[0].toString());
            data.setSlDaBan(x[1].toString());
            list.add(data);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/doanh-thu-theo-quy-3")
    public ResponseEntity<?> getDoanhThuTheoQuy3(){
        List<Object[]> lst = thongKeRepository.getDoanhThuTheoQuy3();
        List<ThongTinThongKe> list = new ArrayList<>();
        for (Object[] x : lst) {
            ThongTinThongKe  data = new ThongTinThongKe();
            data.setDoanhThu(x[0].toString());
            data.setSlDaBan(x[1].toString());
            list.add(data);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/doanh-thu-theo-quy-4")
    public ResponseEntity<?> getDoanhThuTheoQuy4(){
        List<Object[]> lst = thongKeRepository.getDoanhThuTheoQuy4();
        List<ThongTinThongKe> list = new ArrayList<>();
        for (Object[] x : lst) {
            ThongTinThongKe  data = new ThongTinThongKe();
            data.setDoanhThu(x[0].toString());
            data.setSlDaBan(x[1].toString());
            list.add(data);
        }
        return ResponseEntity.ok(list);
    }
}
