package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.config.vnpay.VnPayConfig;
import com.example.websonserver.constants.Constants;
import com.example.websonserver.dto.request.HoaDonRequest;
import com.example.websonserver.dto.request.ThanhToanRequest;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.dto.response.ThanhToanRes;
import com.example.websonserver.entity.HoaDon;
import com.example.websonserver.entity.HoaDonChiTiet;
import com.example.websonserver.entity.PhuongThucThanhToan;
import com.example.websonserver.entity.SanPhamChiTiet;
import com.example.websonserver.repository.HoaDonRepository;
import com.example.websonserver.repository.PhuongThucThanhToanRepository;
import com.example.websonserver.repository.SanPhamChiTietRepository;
import com.example.websonserver.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VnPayServiceImpl {
    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    public HoaDon creatPayment(HoaDonRequest request, HttpServletRequest servletRequest,List<Long> maSanPhamCtList ) throws UnsupportedEncodingException {
        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository.findByTenPhuongThuc(request.getTenPhuongThuc());
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(request.getMaHoaDon());
        hoaDon.setTenNguoiNhan(request.getTenNguoiNhan());
        hoaDon.setTrangThai(request.getTrangThai());
        hoaDon.setXoa(request.getXoa());
        hoaDon.setSdt(request.getSdt());
        hoaDon.setDiaChi(request.getDiaChi());
        hoaDon.setTinh(request.getTinh());
        hoaDon.setHuyen(request.getHuyen());
        hoaDon.setXa(request.getXa());
        hoaDon.setPhuongThucThanhToan(phuongThucThanhToan);
        hoaDon.setNguoiDung(null);
        hoaDon.setEmail(request.getEmail());


        List<HoaDonChiTiet> hoaDonChiTietList = new ArrayList<>();

        StringBuilder sanPhamInfo = new StringBuilder("Danh sách sản phẩm đã mua:\n\n");
        for (Long maSanPhamCt : maSanPhamCtList) {
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(maSanPhamCt).orElse(null);

            if (spct != null) {
                HoaDonChiTiet chiTiet = new HoaDonChiTiet();
                chiTiet.setHoaDon(hoaDon);
                chiTiet.setDonGia(spct.getGiaBan());
                chiTiet.setSanPhamChiTiet(spct);
                chiTiet.setSoLuong(request.getSoLuong());
                if (request.getSoLuong() > spct.getSoLuongTon()) {
                    String errorMessage = "Số lượng sản phẩm vượt giới hạn.";
                    throw new RuntimeException(errorMessage);
                }
                if (spct.getSoLuongTon() - request.getSoLuong() >= 0) {
                    spct.setSoLuongTon(spct.getSoLuongTon() - request.getSoLuong());
                    sanPhamChiTietRepository.save(spct);
                    hoaDonChiTietList.add(chiTiet);
                } else {
                    String errorMessage = "Số lượng vượt giới hạn.";
                    throw new RuntimeException(errorMessage);
                }
                sanPhamInfo.append("Tên sản phẩm: ").append(spct.getSanPham().getTenSanPham()).append("\n");
                sanPhamInfo.append("Số lượng: ").append(chiTiet.getSoLuong()).append("\n");
                sanPhamInfo.append("Đơn giá: ").append(chiTiet.getDonGia()).append("\n");
                sanPhamInfo.append("\n-----------------\n");
            }
        }
        String sanPham = sanPhamInfo.toString();
        String otp = "HEVA shop đã nhận đơn hàng \n";
        String tenNguoiNhan = "Tên người nhận: " + request.getTenNguoiNhan() + "\n";
        String diaChiNha = "Đia chỉ nhà: " + request.getDiaChi() + ", " + request.getXa() + ", " + request.getHuyen() + ", " + request.getTinh() + "\n";
        String email = "Email: " + request.getEmail() + "\n";
        String sdt = "Số điện thoại: " + request.getSdt() + "\n\n\n";
        String tongTien = "Tổng tiền: " + request.getTongTien() + " VND";

        String message = otp + tenNguoiNhan + diaChiNha + email + sdt + sanPham + tongTien;


//        emailService.sendKhachdatHang(request.getEmail(), message);
        hoaDon.setTrangThai(0);
        hoaDon.setTongTien(request.getTongTien());
        hoaDon.setHoaDonChiTietList(hoaDonChiTietList);
        hoaDon.setThanhToan(0);
         hoaDonRepository.save(hoaDon);
//         payWithVNPAY(request,servletRequest);
       HoaDon hoaDon1=  hoaDonRepository.findById(hoaDon.getMaHoaDon()).orElse(null);
       hoaDon1.setNgayThanhToan(LocalDateTime.now());
       hoaDon1.setThanhToan(Constants.STATUS_PAYMENT.DA_THANH_TOAN);
     return hoaDonRepository.save(hoaDon1);
    }

    public String payWithVNPAY(ThanhToanRes payModel , HttpServletRequest request) throws UnsupportedEncodingException {
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        Long amout = payModel.tongTien * 100;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Constants.VnPayConstant.vnp_Version);
        vnp_Params.put("vnp_Command", Constants.VnPayConstant.vnp_Command);
        vnp_Params.put("vnp_TmnCode", Constants.VnPayConstant.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amout));
        vnp_Params.put("vnp_BankCode", Constants.VnPayConstant.vnp_BankCode);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode", Constants.VnPayConstant.vnp_CurrCode);
        vnp_Params.put("vnp_IpAddr", VnPayConfig.getIpAddress(request));
        vnp_Params.put("vnp_Locale", Constants.VnPayConstant.vnp_Locale);
        vnp_Params.put("vnp_OrderInfo", payModel.moTa);
        vnp_Params.put("vnp_OrderType", VnPayConfig.getRandomNumber(8));
        vnp_Params.put("vnp_ReturnUrl", Constants.VnPayConstant.vnp_ReturnUrl);
        vnp_Params.put("vnp_TxnRef", VnPayConfig.getRandomNumber(8));
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldList = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldList);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator itr = fieldList.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append("=");
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append("=");
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append("&");
                    hashData.append("&");
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(Constants.VnPayConstant.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Constants.VnPayConstant.vnp_Url + "?" + queryUrl;

        return paymentUrl;
    }




    public HoaDon vnpayReturn(Long maHoaDon) {
        HoaDon hoaDon = this.hoaDonRepository.findById(maHoaDon).orElse(null);
        hoaDon.setThanhToan(Constants.STATUS_PAYMENT.DA_THANH_TOAN);
        hoaDon.setNgayThanhToan(LocalDateTime.now());
        return hoaDonRepository.save(hoaDon);
    }

}
