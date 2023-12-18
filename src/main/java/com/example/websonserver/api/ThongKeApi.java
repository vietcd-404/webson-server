package com.example.websonserver.api;

import com.example.websonserver.dto.response.NguoiDungResponse;
import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.dto.response.ThongKeResponse.ThongTinThongKe;
import com.example.websonserver.entity.HoaDonChiTiet;
import com.example.websonserver.entity.SanPhamChiTiet;
import com.example.websonserver.repository.HoaDonChiTietRepository;
import com.example.websonserver.repository.ThongKeRepository;
import com.example.websonserver.service.serviceIpml.DanhSachYeuThichServiceImpl;
import com.example.websonserver.service.serviceIpml.HoaDonServiceIpml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin")
public class ThongKeApi {
    @Autowired
    ThongKeRepository thongKeRepository;

    @Autowired
    HoaDonServiceIpml hoaDonService;

    @Autowired
    DanhSachYeuThichServiceImpl danhSachYeuThichService;

    @GetMapping("/doanh-thu-theo-nam")
    public ResponseEntity<?> getDoanhThuTheoNam() {
        List<ThongTinThongKe> list = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = currentYear-4; year <= currentYear; year++) {
            List<Object[]> lst = thongKeRepository.getDoanhThuTheoNam(year);
            ThongTinThongKe data1 = new ThongTinThongKe();
            if (lst.isEmpty()) {
                data1.setDoanhThu("0");
                data1.setSlDaBan("0");
                data1.setYear(year);
                list.add(data1);
            } else {
                for (Object[] x : lst) {
                    ThongTinThongKe data = new ThongTinThongKe();
                    data.setDoanhThu(x[0].toString());
                    data.setSlDaBan(x[1].toString());
                    data.setYear(year);
                    list.add(data);
                }
            }
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/doanh-thu-theo-thang")
    public ResponseEntity<?> getDoanhThuTheoThang(@RequestParam Integer month,@RequestParam Integer year ,@RequestParam String trangThai){
        Integer trangThaiValue = null;
        if (trangThai != null && !trangThai.isEmpty()) {
            try {
                trangThaiValue = Integer.valueOf(trangThai);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        List<Object[]> lst = thongKeRepository.getDoanhThuTheoThang(month,year,trangThaiValue);
        ThongTinThongKe thongKe = new ThongTinThongKe();
        if(lst.isEmpty()) {
        thongKe.setDoanhThu("0");
        thongKe.setSlDaBan("0");
        }
        else{
            for (Object[] x : lst) {
                thongKe.setDoanhThu(x[0].toString());
                thongKe.setSlDaBan(x[1].toString());
                thongKe.setYear(year);
                thongKe.setMonth(month);
            }
        }
        return ResponseEntity.ok(thongKe);
    }
    @GetMapping("/doanh-thu-theo-ngay")
    public ResponseEntity<?> getDoanhThuTheoNgay(@RequestParam String day,@RequestParam String trangThai) {
        ThongTinThongKe thongKe = new ThongTinThongKe();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(day, formatter);
        Integer trangThaiValue = null;
        if (trangThai != null && !trangThai.isEmpty()) {
            try {
                trangThaiValue = Integer.valueOf(trangThai);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        List<Object[]> lst = thongKeRepository.getDoanhThuTheoNgay(trangThaiValue,date);

        if (lst.isEmpty()) {
            thongKe.setDoanhThu("0");
            thongKe.setSlDaBan("0");
        }
        else {
            Object[] result = lst.get(0);
            thongKe.setDoanhThu(result[0].toString());
            thongKe.setSlDaBan(result[1].toString());
        }

        return ResponseEntity.ok(thongKe);
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

    @GetMapping("/top-4-product")
    public ResponseEntity<?> getTop4Product() {
        List<SanPhamChiTietResponse> top4Products = hoaDonService.findTop4BanChay();

        if (top4Products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm nào.");
        }

        return ResponseEntity.ok(top4Products);
    }

    @GetMapping("/total-all-bill")
    public ResponseEntity<?> getTotalBill() {
        return ResponseEntity.ok(hoaDonService.sumTotalBill());
    }

    @GetMapping("/top-4-customer")
    public ResponseEntity<?> getTop4Customer() {
        List<NguoiDungResponse> top4Customer = hoaDonService.findTop4NguoiMua();

        if (top4Customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng  nào.");
        }

        return ResponseEntity.ok(top4Customer);
    }

    @GetMapping("/top-4-favorite")
    public ResponseEntity<?> getTop4Favorite() {
        List<SanPhamChiTietResponse> top4Favorite = danhSachYeuThichService.thongKeTopSanPhamYeuThich();

        if (top4Favorite.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng  nào.");
        }

        return ResponseEntity.ok(top4Favorite);
    }

    @GetMapping("/status-bill")
    public ResponseEntity<?> getCountStatus(@RequestParam Integer status) {
        return ResponseEntity.ok(thongKeRepository.countByTrangThai(status));
    }

    @GetMapping("/total-round-time")
    public ResponseEntity<?> getRoundTime(@RequestParam String ngayBD,@RequestParam String ngayKT,@RequestParam Integer trangThai) {
        ThongTinThongKe thongKe = new ThongTinThongKe();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ngayBD1 = LocalDate.parse(ngayBD, formatter);
        LocalDate ngayKT1 = LocalDate.parse(ngayKT, formatter);
        List<Object[]> lst = thongKeRepository.getDoanhThuTheoKhoangNgay(trangThai,ngayBD1,ngayKT1);

        if (lst.isEmpty()) {
            thongKe.setDoanhThu("0");
            thongKe.setSlDaBan("0");
        }
        else {
            Object[] result = lst.get(0);
            thongKe.setDoanhThu(result[0].toString());
            thongKe.setSlDaBan(result[1].toString());
        }

        return ResponseEntity.ok(thongKe);
    }
}
