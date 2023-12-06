package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.config.email.EmailService;
import com.example.websonserver.constants.Constants;
import com.example.websonserver.dto.request.*;
import com.example.websonserver.dto.response.*;
import com.example.websonserver.entity.*;
import com.example.websonserver.exceptions.NotFoundException;
import com.example.websonserver.repository.*;
import com.example.websonserver.service.GioHangCTSessionService;
import com.example.websonserver.service.HoaDonService;
import com.example.websonserver.service.SanPhamChiTietService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Log4j2
public class HoaDonServiceIpml implements HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private NguoiDungServiceImpl nguoiDungService;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;
    @Autowired
    private GioHangCTSessionService gioHangCTSessionService;

    @Autowired
    private AnhSanPhamServiceImpl anhSanPhamService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;


    @Override
    public HoaDon placeOrder(HoaDonRequest request, Long maGioHang) {
        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository.findByTenPhuongThuc(request.getTenPhuongThuc());
        GioHang gioHang = this.gioHangRepository.findById(maGioHang).orElse(null);
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
        hoaDon.setNguoiDung(gioHang.getNguoiDung());
        List<HoaDonChiTiet> hoaDonChiTietList = new ArrayList<>();
        BigDecimal tongTien = BigDecimal.ZERO.round((new MathContext(1, RoundingMode.HALF_EVEN)));
        for (GioHangChiTiet gioHangChiTiet : gioHang.getCartDetails()) {
            SanPhamChiTiet spct = sanPhamChiTietService.findById(gioHangChiTiet.getSanPhamChiTiet().getMaSanPhamCT().toString());
            HoaDonChiTiet chiTiet1 = new HoaDonChiTiet();
            chiTiet1.setHoaDon(hoaDon);
            if (spct.getPhanTramGiam() > 0) {
                BigDecimal giaSauGiam = spct.getGiaBan()
                        .multiply(BigDecimal.valueOf(100 - spct.getPhanTramGiam()).divide(BigDecimal.valueOf(100)));
                chiTiet1.setDonGia(giaSauGiam);
            } else {
                chiTiet1.setDonGia(spct.getGiaBan());
            }
            chiTiet1.setSanPhamChiTiet(gioHangChiTiet.getSanPhamChiTiet());
            chiTiet1.setSoLuong(gioHangChiTiet.getSoLuong());
            chiTiet1.setTrangThai(gioHangChiTiet.getTrangThai());
            hoaDonChiTietList.add(chiTiet1);
            BigDecimal giaBan = gioHangChiTiet.getSanPhamChiTiet().getGiaBan()
                    .multiply(BigDecimal.valueOf(100 - gioHangChiTiet.getSanPhamChiTiet().getPhanTramGiam()).divide(BigDecimal.valueOf(100)));
            tongTien = tongTien.add(giaBan.multiply(BigDecimal.valueOf(chiTiet1.getSoLuong())));
            //trừ số lượng tồn
            spct.setSoLuongTon(spct.getSoLuongTon() - gioHangChiTiet.getSoLuong());
            sanPhamChiTietRepository.save(spct);
        }
        Voucher voucher = null;
        if (request.getTenVoucher() != null) {
            voucher = voucherRepository.findByTenVoucher(request.getTenVoucher());
        }

        BigDecimal tongTienSauGiamGia = BigDecimal.ZERO;
        if (voucher != null) {
            if (tongTien.compareTo(voucher.getDieuKien()) >= 0) {
                // Điều kiện đủ để áp dụng voucher

                BigDecimal phanTramGiam = voucher.getGiaTriGiam(); // Lấy phần trăm giảm giá từ voucher
                BigDecimal giamToiDa = voucher.getGiamToiDa(); // Lấy giá trị giảm giá tối đa từ voucher

                // Tính giảm giá theo phần trăm
                BigDecimal giamGia = tongTien.multiply(phanTramGiam.divide(BigDecimal.valueOf(100)));

                // Kiểm tra nếu giảm giá vượt quá giới hạn tối đa
                if (giamGia.compareTo(giamToiDa) > 0) {
                    giamGia = giamToiDa; // Giảm giá không vượt quá giới hạn tối đa
                }
                // Áp dụng giảm giá vào tổng tiền hóa đơn
                tongTienSauGiamGia = tongTien.subtract(giamGia);

                List<VoucherChiTiet> voucherChiTiets = new ArrayList<>();
                VoucherChiTiet voucherChiTiet1 = new VoucherChiTiet();
                voucherChiTiet1.setHoaDon(hoaDon);
                voucherChiTiet1.setVoucher(voucher);
                voucherChiTiet1.setSoTienConLai(tongTienSauGiamGia);
                voucherChiTiet1.setTrangThai(0);
                voucherChiTiet1.setXoa(false);
                voucherChiTiets.add(voucherChiTiet1);
                voucher.setSoLuong(voucher.getSoLuong() - 1); // Giảm số lượng voucher còn lại
                voucherRepository.save(voucher);
                hoaDon.setTienGiam(giamGia);

                hoaDon.setVoucherChiTiets(voucherChiTiets);
            } else {
                String errorMessage = "Không đủ điểu kiện hợp lệ.";
                throw new RuntimeException(errorMessage);
            }
        }
        hoaDon.setTongTien(voucher != null ? tongTienSauGiamGia : tongTien);
        hoaDon.setHoaDonChiTietList(hoaDonChiTietList);
        hoaDon.setThanhToan(Constants.STATUS_PAYMENT.CHUA_THANH_TOAN);
        gioHangRepository.deleteById(maGioHang);
        return hoaDonRepository.save(hoaDon);
    }

    public HoaDon taoHoaDonTaiQuay(HoaDonRequest request) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setThanhToan(Constants.STATUS_PAYMENT.HOA_DON_CHO_THANH_TOAN_TAI_QUAY);
        hoaDon.setTrangThai(Constants.STATUS_ORDER.HOA_DON_TAI_QUAY);
        hoaDon.setXoa(true);
        return hoaDonRepository.save(hoaDon);
    }

    public List<HoaDon> getAllHoaDonTaiQuay() {
        List<HoaDon> list = hoaDonRepository.findByTrangThai(Constants.STATUS_ORDER.HOA_DON_TAI_QUAY);
        if (list.size() > 0) {
            list.get(0);
        }
        return list;
    }

    public HoaDon thanhToanGuest(HoaDonRequest request, List<Long> maSanPhamCtList) {
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
        for (int i = 0; i < maSanPhamCtList.size(); i++) {
            Long maSanPhamCt = maSanPhamCtList.get(i);
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(maSanPhamCt).orElse(null);

            if (spct != null) {
                HoaDonChiTiet chiTiet = new HoaDonChiTiet();
                chiTiet.setHoaDon(hoaDon);
                if (spct.getPhanTramGiam() > 0) {
                    BigDecimal giaSauGiam = spct.getGiaBan()
                            .multiply(BigDecimal.valueOf(100 - spct.getPhanTramGiam()).divide(BigDecimal.valueOf(100)));
                    chiTiet.setDonGia(giaSauGiam);
                } else {
                    chiTiet.setDonGia(spct.getGiaBan());
                }
                chiTiet.setSanPhamChiTiet(spct);

                // Use the quantity from the request for each product
                int soLuong = request.getSoLuongList().get(i);

                chiTiet.setSoLuong(soLuong);

//                if (soLuong > spct.getSoLuongTon()) {
//                    String errorMessage = "Số lượng sản phẩm vượt giới hạn.";
//                    throw new RuntimeException(errorMessage);
//                }

//                if (spct.getSoLuongTon() - soLuong >= 0) {
//                    spct.setSoLuongTon(spct.getSoLuongTon() - soLuong);
//                    sanPhamChiTietRepository.save(spct);
//                } else {
//                    String errorMessage = "Số lượng vượt giới hạn.";
//                    throw new RuntimeException(errorMessage);
//                }

                hoaDonChiTietList.add(chiTiet);

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


        emailService.sendKhachdatHang(request.getEmail(), message);
        hoaDon.setTrangThai(0);
        hoaDon.setTongTien(request.getTongTien());
        hoaDon.setHoaDonChiTietList(hoaDonChiTietList);
        hoaDon.setThanhToan(0);
        return hoaDonRepository.save(hoaDon);
    }


//        public HoaDon thanhToanGuest(HoaDonRequest request) {
//            PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository.findByTenPhuongThuc(request.getTenPhuongThuc());
//            HoaDon hoaDon = new HoaDon();
//            hoaDon.setMaHoaDon(request.getMaHoaDon());
//            hoaDon.setTenNguoiNhan(request.getTenNguoiNhan());
//            hoaDon.setTrangThai(request.getTrangThai());
//            hoaDon.setXoa(request.getXoa());
//            hoaDon.setSdt(request.getSdt());
//            hoaDon.setDiaChi(request.getDiaChi());
//            hoaDon.setTinh(request.getTinh());
//            hoaDon.setHuyen(request.getHuyen());
//            hoaDon.setXa(request.getXa());
//            hoaDon.setPhuongThucThanhToan(phuongThucThanhToan);
//            hoaDon.setNguoiDung(null);
//
//            List<HoaDonChiTiet> hoaDonChiTietList = new ArrayList<>();
//            List<SanPhamChiTiet> sanPhamChiTietList = new ArrayList<>();
//
//            for (SanPhamChiTiet sanPhamChiTiet : sanPhamChiTietList) {
//                SanPhamChiTiet spct = sanPhamChiTietRepository.findById(sanPhamChiTiet.getMaSanPhamCT()).orElse(null);
//
//                if (spct != null) {
//                    HoaDonChiTiet chiTiet = new HoaDonChiTiet();
//                    chiTiet.setHoaDon(hoaDon);
//                    chiTiet.setDonGia(spct.getGiaBan());
//                    chiTiet.setSanPhamChiTiet(spct);
//                    chiTiet.setSoLuong(spct.getSoLuongTon());
//                    spct.setSoLuongTon(spct.getSoLuongTon() - chiTiet.getSoLuong());
//                    sanPhamChiTietRepository.save(spct);
//                    hoaDonChiTietList.add(chiTiet);
//                }
//            }
//
//            hoaDon.setTongTien(request.getTongTien());
//            hoaDon.setHoaDonChiTietList(hoaDonChiTietList);
//            hoaDon.setTrangThai(0);
//
//            return hoaDonRepository.save(hoaDon);
//    }

    @Override
    public HoaDon statusHoaDon(HoaDonRequest request, Long maHoaDon) {

        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);

        if (hoaDon == null) {
            throw new NotFoundException("Hóa đơn với mã " + maHoaDon + " không tồn tại");
        }
        int newStatus = request.getTrangThai();

        switch (newStatus) {
            case Constants.STATUS_ORDER.CHO_XAC_NHAN:
                hoaDon.setTrangThai(Constants.STATUS_ORDER.CHO_XAC_NHAN);
                break;
            case Constants.STATUS_ORDER.XAC_NHAN:
                hoaDon.setTrangThai(Constants.STATUS_ORDER.XAC_NHAN);
                break;
            case Constants.STATUS_ORDER.DANG_GIAO:
                hoaDon.setTrangThai(Constants.STATUS_ORDER.DANG_GIAO);
                break;
            case Constants.STATUS_ORDER.HOAN_THANH:
                hoaDon.setTrangThai(Constants.STATUS_ORDER.HOAN_THANH);
                break;
            case Constants.STATUS_ORDER.DA_HUY:
                hoaDon.setTrangThai(Constants.STATUS_ORDER.DA_HUY);
                break;
            default:
                throw new NotFoundException("Invalid status: " + newStatus);
        }
        return hoaDonRepository.save(hoaDon);
    }

    @Override
    public List<HoaDonResponse> getOrdersByUser(Principal principal, Integer trangThai) {
        NguoiDung nguoiDung = nguoiDungService.findByUsername(principal.getName());
        List<HoaDon> orderList = trangThai != null ? hoaDonRepository.findByNguoiDungAndTrangThai(nguoiDung, trangThai) : hoaDonRepository.findAll();

        List<HoaDonResponse> orderResponses = new ArrayList<>();
        for (HoaDon hoaDon : orderList) {
            HoaDonResponse dto = new HoaDonResponse();
            dto.setMaHoaDon(hoaDon.getMaHoaDon());
            dto.setNgayNhan(hoaDon.getNgayNhan());
            dto.setNgayTao(hoaDon.getNgayTao());
            dto.setNgayThanhToan(hoaDon.getNgayThanhToan());
            dto.setTenNguoiDung(hoaDon.getNguoiDung().getUsername());
            dto.setTenPhuongThucThanhToan(hoaDon.getPhuongThucThanhToan().getTenPhuongThuc());
            dto.setDiaChi(hoaDon.getDiaChi());
            dto.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
            dto.setSdt(hoaDon.getSdt());
            dto.setTrangThai(hoaDon.getTrangThai());
            dto.setTongTien(hoaDon.getTongTien());
            dto.setTienGiam(hoaDon.getTienGiam());
            dto.setThanhToan(hoaDon.getThanhToan());
            orderResponses.add(dto);
        }
        return orderResponses;
    }

    public List<HoaDonResponse> getOrdersAllOk(Principal principal) {

        Sort sort = Sort.by(Sort.Direction.DESC, "ngayTao");
        NguoiDung nguoiDung = nguoiDungService.findByUsername(principal.getName());
        List<HoaDon> orderList = hoaDonRepository.findByNguoiDung(nguoiDung, sort);
        List<HoaDonResponse> orderResponses = new ArrayList<>();
        for (HoaDon hoaDon : orderList) {
            HoaDonResponse dto = new HoaDonResponse();
            dto.setMaHoaDon(hoaDon.getMaHoaDon());
            dto.setTongTien(hoaDon.getTongTien());
            dto.setTrangThai(hoaDon.getTrangThai());
            dto.setNgayTao(hoaDon.getNgayTao());
            orderResponses.add(dto);
        }
//        messagingTemplate.convertAndSend("/topic/orders", orderResponses);
        return orderResponses;
    }

    public Page<HoaDonResponse> getAllOrderByAdmin(Pageable pageable, Integer trangThai) {
        Page<HoaDon> orderList = hoaDonRepository.findAllByXoaFalseAndTrangThaiOrderByNgayTaoDesc(trangThai, pageable);

        return orderList.map(hoaDon -> {
            HoaDonResponse dto = new HoaDonResponse();
            dto.setMaHoaDon(hoaDon.getMaHoaDon());
            dto.setTongTien(hoaDon.getTongTien());
            dto.setTrangThai(hoaDon.getTrangThai());
            dto.setNgayTao(hoaDon.getNgayTao());
            dto.setThanhToan(hoaDon.getThanhToan());
            String fullName = "";
            if (hoaDon.getNguoiDung() == null) {
                fullName = "Khách Ngoài";
            } else {
                fullName = hoaDon.getNguoiDung().getHo() + " " + hoaDon.getNguoiDung().getTenDem() + " " + hoaDon.getNguoiDung().getTen();
            }
            dto.setTenNguoiDung(fullName);
            if (hoaDon.getThanhToan() == 1) {
                dto.setNgayThanhToan(hoaDon.getNgayThanhToan());
            }
            return dto;
        });
    }

    public List<HoaDonResponse> orderDetail(Long maHoaDon) {

        List<HoaDon> list = hoaDonRepository.findByMaHoaDon(maHoaDon);
        List<HoaDonResponse> hoaDonResponses = new ArrayList<>();

        for (HoaDon hoaDon : list) {
            HoaDonResponse response = new HoaDonResponse();
            response.setMaHoaDon(hoaDon.getMaHoaDon());
            response.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
            response.setSdt(hoaDon.getSdt());
            response.setTinh(hoaDon.getTinh());
            response.setDiaChi(hoaDon.getDiaChi());
            response.setNguoiDung(hoaDon.getNguoiDung());
            if (hoaDon.getEmail() == null) {
                if (hoaDon.getNguoiDung() != null) {
                    response.setEmail(hoaDon.getNguoiDung().getEmail());

                } else {
                    response.setEmail("123@gmail.com");
                }
            } else {
                response.setEmail(hoaDon.getEmail());
            }
            response.setDiaChiChiTiet(hoaDon.getDiaChi() + ", " + hoaDon.getXa() + ", " + hoaDon.getHuyen() + ", " + hoaDon.getTinh());
            response.setTongTien(hoaDon.getTongTien());
            response.setTenPhuongThucThanhToan(hoaDon.getPhuongThucThanhToan().getTenPhuongThuc());
            response.setHuyen(hoaDon.getHuyen());
            response.setXa(hoaDon.getXa());
            response.setNgayTao(hoaDon.getNgayTao());
            String fullName = "";
            if (hoaDon.getNguoiDung() == null) {
                fullName = "Khách Ngoài";
            } else {
                fullName = hoaDon.getNguoiDung().getHo() + " " + hoaDon.getNguoiDung().getTenDem() + " " + hoaDon.getNguoiDung().getTen();
            }
            response.setTenNguoiDung(fullName);
            response.setTrangThai(hoaDon.getTrangThai());
            hoaDonResponses.add(response);
        }
        return hoaDonResponses;

    }

    public List<HoaDonChiTietResponse> getOrdersDetail(Long maHoaDon) {
//        NguoiDung nguoiDung = nguoiDungService.findByUsername(principal.getName());
        List<HoaDon> orderList = hoaDonRepository.findByMaHoaDon(maHoaDon);
        List<HoaDonChiTietResponse> hoaDonChiTietResponses = new ArrayList<>();
        for (HoaDon hoaDon : orderList) {
            List<HoaDonChiTiet> hoaDonChiTietList = this.hoaDonChiTietRepository.findByHoaDon(hoaDon);
            List<VoucherChiTiet> voucherChiTiets = hoaDon.getVoucherChiTiets();
            if (voucherChiTiets != null && !voucherChiTiets.isEmpty()) {
                for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
                    for (VoucherChiTiet voucherChiTiet : voucherChiTiets) {
                        HoaDonChiTietResponse dto = new HoaDonChiTietResponse();
                        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(hoaDonChiTiet.getSanPhamChiTiet().getMaSanPhamCT()).orElse(null);
                        dto.setAnh(anhSanPhamService.getImagesBySanPhamChiTiet(sanPhamChiTiet.getMaSanPhamCT()));
                        dto.setTenSanPham("[" + hoaDonChiTiet.getSanPhamChiTiet().getMauSac().getTenMau() + "]" + " " + hoaDonChiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham());
                        dto.setSoLuong(hoaDonChiTiet.getSoLuong());
                        dto.setDonGia(hoaDonChiTiet.getDonGia());
                        dto.setTenPhuongThucThanhToan(hoaDonChiTiet.getHoaDon().getPhuongThucThanhToan().getTenPhuongThuc());
                        dto.setGiaBan(hoaDonChiTiet.getDonGia());
                        dto.setPhanTramGiam(hoaDonChiTiet.getSanPhamChiTiet().getPhanTramGiam());
                        dto.setSoLuongTon(hoaDonChiTiet.getSanPhamChiTiet().getSoLuongTon());
                        dto.setMaHoaDonCT(hoaDonChiTiet.getMaHDCT());
                        dto.setMaHoaDon(hoaDon.getMaHoaDon());
                        dto.setTrangThai(hoaDon.getTrangThai());
                        dto.setMaSanPhamCT(hoaDonChiTiet.getSanPhamChiTiet().getMaSanPhamCT());
                        dto.setTongTien(hoaDon.getTongTien());
                        dto.setTienGiam(hoaDon.getTienGiam());
                        dto.setDieuKien(voucherChiTiet.getVoucher().getDieuKien());
                        hoaDonChiTietResponses.add(dto);
                    }
                }
            } else {
                for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
                    HoaDonChiTietResponse dto = new HoaDonChiTietResponse();
                    SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(hoaDonChiTiet.getSanPhamChiTiet().getMaSanPhamCT()).orElse(null);
                    dto.setAnh(anhSanPhamService.getImagesBySanPhamChiTiet(sanPhamChiTiet.getMaSanPhamCT()));
                    dto.setTenSanPham("[" + hoaDonChiTiet.getSanPhamChiTiet().getMauSac().getTenMau() + "]" + " " + hoaDonChiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham());
                    dto.setSoLuong(hoaDonChiTiet.getSoLuong());
                    dto.setDonGia(hoaDonChiTiet.getDonGia());
                    dto.setTenPhuongThucThanhToan(hoaDonChiTiet.getHoaDon().getPhuongThucThanhToan().getTenPhuongThuc());
                    dto.setGiaBan(hoaDonChiTiet.getDonGia());
                    dto.setPhanTramGiam(hoaDonChiTiet.getSanPhamChiTiet().getPhanTramGiam());
                    dto.setSoLuongTon(hoaDonChiTiet.getSanPhamChiTiet().getSoLuongTon());
                    dto.setMaHoaDonCT(hoaDonChiTiet.getMaHDCT());
                    dto.setMaHoaDon(hoaDon.getMaHoaDon());
                    dto.setTrangThai(hoaDon.getTrangThai());
                    dto.setMaSanPhamCT(hoaDonChiTiet.getSanPhamChiTiet().getMaSanPhamCT());
                    dto.setTongTien(hoaDon.getTongTien());
                    dto.setTienGiam(hoaDon.getTienGiam());
                    hoaDonChiTietResponses.add(dto);
                }
            }


        }
        return hoaDonChiTietResponses;
    }

    public List<HoaDonChiTietResponse> getOrdersDetai(Long maHoaDon) {
//        NguoiDung nguoiDung = nguoiDungService.findByUsername(principal.getName());
        List<HoaDon> orderList = hoaDonRepository.findByMaHoaDon(maHoaDon);
        List<HoaDonChiTietResponse> hoaDonChiTietResponses = new ArrayList<>();
        for (HoaDon hoaDon : orderList) {
            List<HoaDonChiTiet> hoaDonChiTietList = this.hoaDonChiTietRepository.findByHoaDon(hoaDon);
            for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
                HoaDonChiTietResponse dto = new HoaDonChiTietResponse();
                dto.setTenSanPham(hoaDonChiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham());
                dto.setSoLuong(hoaDonChiTiet.getSoLuong());
                dto.setDonGia(hoaDonChiTiet.getDonGia());
                dto.setGiaBan(hoaDonChiTiet.getSanPhamChiTiet().getGiaBan());
                dto.setTenMau(hoaDonChiTiet.getSanPhamChiTiet().getMauSac().getTenMau());
                dto.setPhanTramGiam(hoaDonChiTiet.getSanPhamChiTiet().getPhanTramGiam());
                dto.setSoLuongTon(hoaDonChiTiet.getSanPhamChiTiet().getSoLuongTon());
                dto.setMaHoaDonCT(hoaDonChiTiet.getMaHDCT());
                dto.setMaHoaDon(hoaDon.getMaHoaDon());
                dto.setTrangThai(hoaDon.getTrangThai());
                dto.setMaSanPhamCT(hoaDonChiTiet.getSanPhamChiTiet().getMaSanPhamCT());
                dto.setTienGiam(hoaDon.getTienGiam());
                hoaDonChiTietResponses.add(dto);
            }
        }
        return hoaDonChiTietResponses;
    }


    @Override
    public HoaDonChiTiet updateQuantity(Long idSPCT, int soLuong) {
        return null;
    }

    @Override
    public String huyHoaDon(Long maHD) {
        HoaDon hd = hoaDonRepository.findById(maHD).orElse(null);
        if (hd.getTrangThai() == 0 || hd.getTrangThai() == 1) {

            hd.setTrangThai(Constants.STATUS_ORDER.DA_HUY);
            hoaDonRepository.save(hd);
            if (hd.getTrangThai() == Constants.STATUS_ORDER.DA_HUY) {
                List<HoaDonChiTiet> hoaDonChiTietList = this.hoaDonChiTietRepository.findByHoaDon_MaHoaDon(maHD);

                for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
                    SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
                    int soLuongTon = hoaDonChiTiet.getSanPhamChiTiet().getSoLuongTon();
                    int soLuong = hoaDonChiTiet.getSoLuong();
                    int moi = soLuongTon + soLuong;
                    sanPhamChiTiet.setSoLuongTon(moi);
                    sanPhamChiTietRepository.save(sanPhamChiTiet);

                }
            }
            if (hd.getTrangThai() == Constants.STATUS_ORDER.DA_HUY && hd.getNguoiDung() == null) {
                List<HoaDon> orderList = hoaDonRepository.findByMaHoaDon(maHD);
                StringBuilder sanPhamInfo = new StringBuilder();
                for (HoaDon hoaDon1 : orderList) {
                    sanPhamInfo.append("Tên người nhận: ").append(hoaDon1.getTenNguoiNhan()).append("\n");
                    sanPhamInfo.append("Email: ").append(hoaDon1.getEmail()).append("\n");
                    sanPhamInfo.append("Địa chỉ nhà: ").append(hoaDon1.getDiaChi()).append(" ").append(hoaDon1.getXa())
                            .append(hoaDon1.getHuyen()).append(hoaDon1.getTinh()).append("\n");
                    sanPhamInfo.append("Số điện thoại: ").append(hoaDon1.getSdt()).append("\n\n");
                    sanPhamInfo.append("Danh sách sản phẩm đã mua:\n\n");
                    List<HoaDonChiTiet> hoaDonChiTietList = this.hoaDonChiTietRepository.findByHoaDon(hd);
                    for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {

                        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(hoaDonChiTiet.getSanPhamChiTiet().getMaSanPhamCT()).orElse(null);
                        sanPhamInfo.append("Tên sản phẩm: ").append(sanPhamChiTiet.getSanPham().getTenSanPham()).append("\n");
                        sanPhamInfo.append("Số lượng: ").append(hoaDonChiTiet.getSoLuong()).append("\n");
                        sanPhamInfo.append("Đơn giá: ").append(hoaDonChiTiet.getDonGia()).append("\n");
                        sanPhamInfo.append("\n-----------------\n");

                    }
                    sanPhamInfo.append("Tổng tiền: ").append(hoaDon1.getTongTien()).append("\n");
                }
                String thongBao = "đã được hủy";
                String sanPham = sanPhamInfo.toString();
                emailService.sendThongBao(hd.getEmail(), sanPham, thongBao);
            }
            return "Bạn đã hủy hóa đơn thành công.";
        } else {
            return "Bạn đơn hàng của bạn đã được xác nhận hoặc không có nên không thể hủy";
        }
    }

    @Override
    public HoaDon hoaDonSession(HttpSession session, NguoiDungSessionRequest request) {
        Map<String, Integer> map = (Map<String, Integer>) session.getAttribute("sessionCart");
        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository.findByTenPhuongThuc(request.getPhuongThucThanhToan());
        List<HoaDonChiTiet> lstHDCT = new ArrayList<>();
        HoaDon hd = new HoaDon();
        hd.setDiaChi(request.getDiaChi());
        hd.setTenNguoiNhan(request.getTenNguoiNhan());
        hd.setTrangThai(Constants.STATUS_ORDER.CHO_XAC_NHAN);
        hd.setXoa(Boolean.FALSE);
        BigDecimal tongTien = BigDecimal.ZERO;
        for (String x : map.keySet()) {
            HoaDonChiTiet chiTiet1 = new HoaDonChiTiet();
            SanPhamChiTiet spct = sanPhamChiTietService.findById(x);
            chiTiet1.setHoaDon(hd);
            chiTiet1.setDonGia(spct.getGiaBan());
            chiTiet1.setSanPhamChiTiet(spct);
            chiTiet1.setSoLuong(map.get(x));
            chiTiet1.setTrangThai(0);
            tongTien = tongTien.add(spct.getGiaBan().multiply(BigDecimal.valueOf(map.get(x))));
            lstHDCT.add(chiTiet1);
            // trừ số lượng tồn
            spct.setSoLuongTon(spct.getSoLuongTon() - map.get(x));
            sanPhamChiTietRepository.save(spct);
        }
        gioHangCTSessionService.clearSessionCart(session);
        hd.setPhuongThucThanhToan(phuongThucThanhToan);
        hd.setTongTien(tongTien);
        hd.setHoaDonChiTietList(lstHDCT);
        hd.setThanhToan(Constants.STATUS_PAYMENT.CHUA_THANH_TOAN);
        hoaDonRepository.save(hd);
        return hd;
    }

    @Override
    public List<HoaDonResponse> findHoaDon(Pageable pageable, String thuocTinh, String value, Integer trangThai) {
        List<HoaDonResponse> hoaDonResponseList = new ArrayList<>();
        List<HoaDon> hoaDonList = new ArrayList<>();
        if (thuocTinh.equals("maHoaDon")) {
            hoaDonList = hoaDonRepository.findByMaHoaDonAndTrangThaiAndXoaIsFalse(pageable, Long.parseLong((value)), trangThai);
        }
        if (thuocTinh.equals("tenNguoiDung")) {
            hoaDonList = hoaDonRepository.searchByHoTen(pageable, value, trangThai);
        }
        if (thuocTinh.equals("ngayTao")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
            LocalDate dateTime = LocalDate.parse(value, formatter);
            hoaDonList = hoaDonRepository.searchByNgayTao(pageable, dateTime, trangThai);
        }

        for (HoaDon hoaDon :
                hoaDonList) {
            HoaDonResponse dto = new HoaDonResponse();
            dto.setMaHoaDon(hoaDon.getMaHoaDon());
            dto.setTongTien(hoaDon.getTongTien());
            dto.setTrangThai(hoaDon.getTrangThai());
            dto.setNgayTao(hoaDon.getNgayTao());
            dto.setThanhToan(hoaDon.getThanhToan());
            String fullName = "";
            if (hoaDon.getNguoiDung() == null) {
                fullName = "Khách Ngoài";
            } else {
                fullName = hoaDon.getNguoiDung().getHo() + " " + hoaDon.getNguoiDung().getTenDem() + " " + hoaDon.getNguoiDung().getTen();
            }
            dto.setTenNguoiDung(fullName);
            if (hoaDon.getThanhToan() == 1) {
                dto.setNgayThanhToan(hoaDon.getNgayThanhToan());
            }
            hoaDonResponseList.add(dto);
        }
        ;
        return hoaDonResponseList;
    }

    @Override
    public HoaDon getHoaDonChiTiet(Long maHoaDon) {
        return null;
    }

    @Override
    public HoaDon updateOrder(UpdateHoaDonRequest request, Long maHoaDon) {
        Optional<HoaDon> optional = hoaDonRepository.findById(maHoaDon);
        return optional.map(o -> {
            if (o.getTrangThai() == 0) {
                o.setTenNguoiNhan(request.getTenNguoiNhan());
                o.setTinh(request.getTinh());
                o.setSdt((request.getSdt()));
                o.setHuyen(request.getHuyen());
                o.setXa(request.getXa());
                o.setDiaChi(request.getDiaChi());
//                List<HoaDonChiTiet> hoaDonChiTiets = o.getHoaDonChiTietList();
//                BigDecimal tongTien = BigDecimal.ZERO;
//                if (hoaDonChiTiets != null && !hoaDonChiTiets.isEmpty()) {
//                    for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
//                        suaSoLuongVaoHoaDon(request.getMaHoaDonCT(), request.getSoLuong());
//                        tongTien = tongTien.add(hoaDonChiTiet.getDonGia().multiply(BigDecimal.valueOf(hoaDonChiTiet.getSoLuong())));
//                    }
//                }
//                BigDecimal tongTienSauGiamGia = BigDecimal.ZERO;
//                List<VoucherChiTiet> voucherChiTiets = o.getVoucherChiTiets();
//
//                if (voucherChiTiets != null && !voucherChiTiets.isEmpty()) {
//                    for (VoucherChiTiet voucherChiTiet : voucherChiTiets) {
//                        Voucher voucher = voucherChiTiet.getVoucher();
//
//                        if (tongTien.compareTo(voucher.getDieuKien()) >= 0) {
//                            BigDecimal phanTramGiam = voucher.getGiaTriGiam();
//                            BigDecimal giamToiDa = voucher.getGiamToiDa();
//
//                            BigDecimal giamGia = tongTien.multiply(phanTramGiam.divide(BigDecimal.valueOf(100)));
//
//                            if (giamGia.compareTo(giamToiDa) > 0) {
//                                giamGia = giamToiDa;
//                            }
//                            tongTienSauGiamGia = tongTien.subtract(giamGia);
//                            o.setTienGiam(giamGia);
//                        }else {
//                            String errorMessage = "Không đủ điểu kiện hợp lệ";
//                            throw new RuntimeException(errorMessage);
//                        }
//                    }
//                    o.setTongTien(tongTienSauGiamGia);
//                }else {
//                    o.setTongTien(tongTien);
//                }

            } else {
                return null;
            }
            return hoaDonRepository.save(o);
        }).orElse(null);
    }

    public Boolean deleteHDCT(Long maHDCT) {
        HoaDonChiTiet hoaDonChiTiet = this.hoaDonChiTietRepository.findById(maHDCT).orElse(null);

        HoaDon hoaDon = hoaDonRepository.findById(hoaDonChiTiet.getHoaDon().getMaHoaDon()).orElse(null);
        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getHoaDonChiTietList();
        List<VoucherChiTiet> voucherChiTiets = hoaDon.getVoucherChiTiets();
        sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() + hoaDonChiTiet.getSoLuong());
        BigDecimal tongTienSauGiamGia = BigDecimal.ZERO;
        BigDecimal tongTien = BigDecimal.ZERO;
        if (voucherChiTiets != null && !voucherChiTiets.isEmpty()) {
            for (VoucherChiTiet voucherChiTiet : voucherChiTiets) {
                BigDecimal tongTienTruoc = hoaDon.getTongTien().add(hoaDon.getTienGiam());
                BigDecimal giaSauGiam = hoaDonChiTiet.getDonGia().multiply(BigDecimal.valueOf((hoaDonChiTiet.getSoLuong())));
                if (voucherChiTiet.getVoucher().getDieuKien().compareTo(tongTienTruoc.subtract(giaSauGiam)) > 0) {
                    String errorMessage = "Không đạt điều kiện voucher!";
                    throw new RuntimeException(errorMessage);
                }
            }
        }
        hoaDonChiTietRepository.deleteById(maHDCT);


        for (HoaDonChiTiet hoaDonChiTieta : hoaDonChiTiets) {
            sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() + hoaDonChiTieta.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            hoaDonChiTieta.setSoLuong(hoaDonChiTieta.getSoLuong());
            hoaDonChiTietRepository.save(hoaDonChiTieta);
            BigDecimal giaBan = hoaDonChiTieta.getSanPhamChiTiet().getGiaBan()
                    .multiply((BigDecimal.valueOf(100 - hoaDonChiTieta.getSanPhamChiTiet().getPhanTramGiam())).divide(BigDecimal.valueOf(100)));
            tongTien = tongTien.add(giaBan.multiply(BigDecimal.valueOf(hoaDonChiTieta.getSoLuong()))
            );
            sanPhamChiTiet.setSoLuongTon(hoaDonChiTiet.getSanPhamChiTiet().getSoLuongTon() - hoaDonChiTieta.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);
        }

        if (voucherChiTiets != null && !voucherChiTiets.isEmpty()) {
            for (VoucherChiTiet voucherChiTiet : voucherChiTiets) {
                Voucher voucher = voucherChiTiet.getVoucher();
                if (voucher != null) {
                    if (tongTien.compareTo(voucher.getDieuKien()) >= 0) {
                        BigDecimal phanTramGiam = voucher.getGiaTriGiam();
                        BigDecimal giamToiDa = voucher.getGiamToiDa();

                        BigDecimal giamGia = tongTien.multiply(phanTramGiam.divide(BigDecimal.valueOf(100)));

                        if (giamGia.compareTo(giamToiDa) > 0) {
                            giamGia = giamToiDa;
                        }
                        tongTienSauGiamGia = tongTien.subtract(giamGia);
                        hoaDon.setTienGiam(giamGia);
                        hoaDon.setTongTien(tongTienSauGiamGia);


                    } else {
                        String errorMessage = "Không đủ điểu kiện hợp lệ";
                        new MessageResponse(errorMessage);
                        return false;
                    }
                }
            }
        } else {
            hoaDon.setTongTien(tongTien);
        }

        hoaDonChiTiets.remove(hoaDonChiTiet);

        hoaDonRepository.save(hoaDon);
        return true;

    }

    public HoaDonChiTiet suaSoLuongVaoHoaDon(Long maHoaDonChiTiet, Integer soLuong, Long maHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);

        HoaDonChiTiet hoaDonChiTiet = this.hoaDonChiTietRepository.findById(maHoaDonChiTiet).orElse(null);
        if (hoaDonChiTiet == null) {
            String errorMessage = "Không tìm thấy mã hóa đơn chi tiết";
            throw new RuntimeException(errorMessage);
        }

        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
        if (sanPhamChiTiet == null) {
            String errorMessage = "Không tìm thấy sản phẩm chi tiết trong hóa đơn chi tiết";
            throw new RuntimeException(errorMessage);
        }
        if(hoaDon.getThanhToan()==1){
            String errorMessage = "Hóa đơn đã thanh toán không thể cập nhập";
            throw new RuntimeException(errorMessage);
        }
        int soLuongTon = sanPhamChiTiet.getSoLuongTon();
        if (soLuong <= 0) {
            String errorMessage = "Số lượng cập nhật không hợp lệ";
            throw new RuntimeException(errorMessage);
        }
        if (soLuong > soLuongTon) {
            String errorMessage = "Số lượng cập nhật vượt quá số lượng tồn kho";
            throw new RuntimeException(errorMessage);
        } else {
            BigDecimal tongTienTruoc = BigDecimal.ZERO;
            BigDecimal tongTien = BigDecimal.ZERO;
            List<VoucherChiTiet> voucherChiTiets = hoaDon.getVoucherChiTiets();
            if (voucherChiTiets != null && !voucherChiTiets.isEmpty()) {
                for (VoucherChiTiet voucherChiTiet : voucherChiTiets) {

                    BigDecimal tienSauGiam = BigDecimal.ZERO;
                    if(hoaDonChiTiet.getSoLuong() < soLuong){
                        tienSauGiam = hoaDonChiTiet.getDonGia().multiply(BigDecimal.valueOf(soLuong - hoaDonChiTiet.getSoLuong()));
                        tongTienTruoc = hoaDon.getTongTien().add(tienSauGiam).add(hoaDon.getTienGiam());
                    }
                    if (hoaDonChiTiet.getSoLuong() > soLuong) {
                        tienSauGiam = hoaDonChiTiet.getDonGia().multiply(BigDecimal.valueOf(hoaDonChiTiet.getSoLuong() - soLuong));
                        tongTienTruoc = hoaDon.getTongTien().subtract(tienSauGiam).add(hoaDon.getTienGiam());
                        if (voucherChiTiet.getVoucher().getDieuKien().compareTo(tongTienTruoc) > 0) {
                            String errorMessage = "Không đạt điều kiện voucher!";
                            throw new RuntimeException(errorMessage);
                        }
                    }

                }
            }

            List<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getHoaDonChiTietList();
            if (hoaDonChiTiets != null && !hoaDonChiTiets.isEmpty()) {
                for (HoaDonChiTiet hoaDonChiTieta : hoaDonChiTiets) {
                    if (hoaDonChiTiet.getSoLuong() < soLuong) {
                        Integer solgSau = soLuong - hoaDonChiTiet.getSoLuong();
                        sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - solgSau);
                    }
                    if (hoaDonChiTiet.getSoLuong() > soLuong) {
                        Integer solgSau = hoaDonChiTiet.getSoLuong() - soLuong;
                        sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() + solgSau);
                    }
                    sanPhamChiTietRepository.save(sanPhamChiTiet);
                    hoaDonChiTiet.setSoLuong(soLuong);
                    hoaDonChiTietRepository.save(hoaDonChiTiet);
                    BigDecimal giaBan = hoaDonChiTieta.getDonGia();
                    tongTien = tongTien.add(giaBan.multiply(BigDecimal.valueOf(hoaDonChiTieta.getSoLuong()))
                    );
                }
            }
            if(tongTienTruoc.compareTo(BigDecimal.ZERO)>0){
                hoaDon.setTongTien(tongTienTruoc.subtract(hoaDon.getTienGiam()));
            }else{
                hoaDon.setTongTien(tongTien);
            }
            hoaDon.setVoucherChiTiets(voucherChiTiets);
            hoaDonRepository.save(hoaDon);

            return hoaDonChiTiet;
        }
    }

    public HoaDonChiTiet suaSoLuongVaoHoaDonTaiQuay(Long maHoaDonChiTiet, Integer soLuong, Long maHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);

        HoaDonChiTiet hoaDonChiTiet = this.hoaDonChiTietRepository.findById(maHoaDonChiTiet).orElse(null);
        if (hoaDonChiTiet == null) {
            String errorMessage = "Không tìm thấy mã hóa đơn chi tiết";
            throw new RuntimeException(errorMessage);
        }

        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
        if (sanPhamChiTiet == null) {
            String errorMessage = "Không tìm thấy sản phẩm chi tiết trong hóa đơn chi tiết";
            throw new RuntimeException(errorMessage);
        }
        int soLuongTon = sanPhamChiTiet.getSoLuongTon();
//        if (soLuongTon <= 0) {
//            String errorMessage = "Số lượng cập nhật vượt quá số lượng tồn kho";
//            throw new RuntimeException(errorMessage);
//        }else {
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getHoaDonChiTietList();
        BigDecimal tongTien = BigDecimal.ZERO;
        if (hoaDonChiTiets != null && !hoaDonChiTiets.isEmpty()) {
            for (HoaDonChiTiet hoaDonChiTieta : hoaDonChiTiets) {
                sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() + hoaDonChiTiet.getSoLuong());
                sanPhamChiTietRepository.save(sanPhamChiTiet);
                hoaDonChiTiet.setSoLuong(soLuong);
                hoaDonChiTietRepository.save(hoaDonChiTiet);
                BigDecimal giaBan = hoaDonChiTieta.getSanPhamChiTiet().getGiaBan()
                        .multiply((BigDecimal.valueOf(100 - hoaDonChiTieta.getSanPhamChiTiet().getPhanTramGiam())).divide(BigDecimal.valueOf(100)));
                tongTien = tongTien.add(giaBan.multiply(BigDecimal.valueOf(hoaDonChiTieta.getSoLuong()))
                );
                sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - soLuong);
                sanPhamChiTietRepository.save(sanPhamChiTiet);
            }
        }
        BigDecimal tongTienSauGiamGia = BigDecimal.ZERO;
        List<VoucherChiTiet> voucherChiTiets = hoaDon.getVoucherChiTiets();
        hoaDon.setTongTien(tongTien);
        hoaDon.setVoucherChiTiets(voucherChiTiets);
        hoaDonRepository.save(hoaDon);
//        }


        return hoaDonChiTiet;
    }

    public HoaDon updateStatus(Integer trangThai, Long maHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);

        if (trangThai == 3) {
            hoaDon.setNgayNhan(LocalDateTime.now());
        }
        if (trangThai == 4) {
//            List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findByHoaDon(hoaDon);
            List<HoaDonChiTiet> hoaDonChiTietList = this.hoaDonChiTietRepository.findByHoaDon_MaHoaDon(maHoaDon);
//            HoaDonChiTiet hoaDonChiTieta = hoaDonChiTietList.size() == 1 ? hoaDonChiTietList.get(0) : null;

            for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
                SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
                int soLuongTon = hoaDonChiTiet.getSanPhamChiTiet().getSoLuongTon();
                int soLuong = hoaDonChiTiet.getSoLuong();
                int moi = soLuongTon + soLuong;
                sanPhamChiTiet.setSoLuongTon(moi);
                sanPhamChiTietRepository.save(sanPhamChiTiet);

            }
        }

        hoaDon.setTrangThai(trangThai);
        if (hoaDon.getTrangThai() == trangThai && hoaDon.getNguoiDung() == null) {
            List<HoaDon> orderList = hoaDonRepository.findByMaHoaDon(maHoaDon);
            StringBuilder sanPhamInfo = new StringBuilder();
            for (HoaDon hoaDon1 : orderList) {
                sanPhamInfo.append("Tên người nhận: ").append(hoaDon1.getTenNguoiNhan()).append("\n");
                sanPhamInfo.append("Email: ").append(hoaDon1.getEmail()).append("\n");
                sanPhamInfo.append("Địa chỉ nhà: ").append(hoaDon1.getDiaChi()).append(" ").append(hoaDon1.getXa())
                        .append(hoaDon1.getHuyen()).append(hoaDon1.getTinh()).append("\n");
                sanPhamInfo.append("Số điện thoại: ").append(hoaDon1.getSdt()).append("\n\n");
                sanPhamInfo.append("Danh sách sản phẩm đã mua:\n\n");
                List<HoaDonChiTiet> hoaDonChiTietList = this.hoaDonChiTietRepository.findByHoaDon(hoaDon);
                for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {

                    SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(hoaDonChiTiet.getSanPhamChiTiet().getMaSanPhamCT()).orElse(null);
                    sanPhamInfo.append("Tên sản phẩm: ").append(sanPhamChiTiet.getSanPham().getTenSanPham()).append("\n");
                    sanPhamInfo.append("Số lượng: ").append(hoaDonChiTiet.getSoLuong()).append("\n");
                    sanPhamInfo.append("Đơn giá: ").append(hoaDonChiTiet.getDonGia()).append("\n");
                    sanPhamInfo.append("\n-----------------\n");

                }
                sanPhamInfo.append("Tổng tiền: ").append(hoaDon1.getTongTien()).append("\n");
            }
            String thongBao = null;
            if (trangThai == 1) {
                thongBao = "đã được xác nhận";
            } else if (trangThai == 2) {
                thongBao = "đang được giao";
            } else if (trangThai == 3) {
                thongBao = "đã được giao thành công";
                hoaDon.setNgayNhan(LocalDateTime.now());
            } else if (trangThai == 4) {
                thongBao = "đã được hủy";
            }
            String sanPham = sanPhamInfo.toString();
            emailService.sendThongBao(hoaDon.getEmail(), sanPham, thongBao);
        }
        HoaDon update = hoaDonRepository.save(hoaDon);
//        messagingTemplate.convertAndSend("/topic/orders", update);
        return update;
    }


//    private void updateSanPhamChiTietQuantity(HoaDonChiTiet hoaDonChiTiet) {
//        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository
//                .findById(hoaDonChiTiet.getSanPhamChiTiet().getMaSanPhamCT())
//                .orElseThrow(() -> new EntityNotFoundException("SanPhamChiTiet not found with ID: "
//                        + hoaDonChiTiet.getSanPhamChiTiet().getMaSanPhamCT()));
//
//        log.debug("Previous quantity: {}, Added quantity: {}", sanPhamChiTiet.getSoLuongTon(), hoaDonChiTiet.getSoLuong());
//
//        // Update the quantity
//        Integer newQuantity = sanPhamChiTiet.getSoLuongTon() + hoaDonChiTiet.getSoLuong();
//        sanPhamChiTiet.setSoLuongTon(newQuantity);
//
//        // Save the updated SanPhamChiTiet
//        sanPhamChiTietRepository.save(sanPhamChiTiet);
//    }

    public HoaDon updatePaid(Integer thanhToan, Long maHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);
        hoaDon.setThanhToan(thanhToan);
        hoaDon.setNgayThanhToan(LocalDateTime.now());
        return hoaDonRepository.save(hoaDon);
    }

    public HoaDon themSanPhamVaoHoaDon(Long maSPCT, Integer soLuong, Long maHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);
        if(hoaDon.getThanhToan()==1){
            String errorMessage = "Hóa đơn đã thanh toán không thể cập nhập";
            throw new RuntimeException(errorMessage);
        }
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(maSPCT).orElse(null);
        if (sanPhamChiTiet == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm chi tiết");
        }

        int soLuongTon = sanPhamChiTiet.getSoLuongTon();
        if (soLuong > soLuongTon) {
            throw new RuntimeException("Số lượng cập nhật vượt quá số lượng tồn kho");
        }

        List<HoaDonChiTiet> hoaDonChiTietList = hoaDon.getHoaDonChiTietList();

        Optional<HoaDonChiTiet> existingChiTiet = hoaDonChiTietList.stream()
                .filter(chiTiet -> chiTiet.getSanPhamChiTiet().getMaSanPhamCT().equals(maSPCT))
                .findFirst();

        if (existingChiTiet.isPresent()) {
            sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - soLuong);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            HoaDonChiTiet chiTietToUpdate = existingChiTiet.get();
            chiTietToUpdate.setSoLuong(chiTietToUpdate.getSoLuong() + soLuong);
        } else {
            HoaDonChiTiet newHoaDonChiTiet = new HoaDonChiTiet();
            newHoaDonChiTiet.setHoaDon(hoaDon);
            if (sanPhamChiTiet.getPhanTramGiam() > 0) {
                BigDecimal giaSauGiam = sanPhamChiTiet.getGiaBan()
                        .multiply(BigDecimal.valueOf(100 - sanPhamChiTiet.getPhanTramGiam()).divide(BigDecimal.valueOf(100)));
                newHoaDonChiTiet.setDonGia(giaSauGiam);
            } else {
                newHoaDonChiTiet.setDonGia(sanPhamChiTiet.getGiaBan());
            }
            newHoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            newHoaDonChiTiet.setSoLuong(soLuong);
            newHoaDonChiTiet.setTrangThai(0);
            newHoaDonChiTiet.setXoa(Boolean.FALSE);
            sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - soLuong);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            hoaDonChiTietList.add(newHoaDonChiTiet);
        }


        updateTotalAmount(hoaDon);
//        hoaDonChiTietRepository.save(hoaDonChiTietList);

        return hoaDonRepository.save(hoaDon);
    }

    private void updateTotalAmount(HoaDon hoaDon) {
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getHoaDonChiTietList();
        BigDecimal tongTien = BigDecimal.ZERO;

        for (HoaDonChiTiet hoaDonChiTieta : hoaDonChiTiets) {
            BigDecimal giaBan = hoaDonChiTieta.getSanPhamChiTiet().getGiaBan()
                    .multiply((BigDecimal.valueOf(100 - hoaDonChiTieta.getSanPhamChiTiet().getPhanTramGiam())).divide(BigDecimal.valueOf(100)));
            tongTien = tongTien.add(giaBan.multiply(BigDecimal.valueOf(hoaDonChiTieta.getSoLuong())));
        }

        BigDecimal tongTienSauGiamGia = BigDecimal.ZERO;
        List<VoucherChiTiet> voucherChiTiets = hoaDon.getVoucherChiTiets();

        if (voucherChiTiets != null && !voucherChiTiets.isEmpty()) {
            for (VoucherChiTiet voucherChiTiet : voucherChiTiets) {
                Voucher voucher = voucherChiTiet.getVoucher();
                if (voucher != null) {
                    if (tongTien.compareTo(voucher.getDieuKien()) >= 0) {
                        BigDecimal phanTramGiam = voucher.getGiaTriGiam();
                        BigDecimal giamToiDa = voucher.getGiamToiDa();
                        BigDecimal giamGia = tongTien.multiply(phanTramGiam.divide(BigDecimal.valueOf(100)));
                        if (giamGia.compareTo(giamToiDa) > 0) {
                            giamGia = giamToiDa;
                        }
                        tongTienSauGiamGia = tongTien.subtract(giamGia);
                        hoaDon.setTienGiam(giamGia);
                        hoaDon.setTongTien(tongTienSauGiamGia);
                    } else {
                        String errorMessage = "Không đủ điều kiện hợp lệ";
                        throw new RuntimeException(errorMessage);
                    }
                } else {
                    hoaDon.setTongTien(tongTien);
                }
            }
        } else {
            hoaDon.setTongTien(tongTien);
        }

        hoaDon.setVoucherChiTiets(voucherChiTiets);
    }


    public Page<HoaDonResponse> findAllHd(Pageable pageable) {
        Page<HoaDon> orderList = hoaDonRepository.findAllByXoaFalseOrderByNgayTaoDesc(pageable);

        return orderList.map(hoaDon -> {
            HoaDonResponse dto = new HoaDonResponse();
            dto.setMaHoaDon(hoaDon.getMaHoaDon());
            dto.setTongTien(hoaDon.getTongTien());
            dto.setTrangThai(hoaDon.getTrangThai());
            dto.setNgayTao(hoaDon.getNgayTao());
            dto.setThanhToan(hoaDon.getThanhToan());
            String fullName = "";
            if (hoaDon.getNguoiDung() == null) {
                fullName = "Khách Ngoài";
            } else {
                fullName = hoaDon.getNguoiDung().getHo() + " " + hoaDon.getNguoiDung().getTenDem() + " " + hoaDon.getNguoiDung().getTen();
            }
            dto.setTenNguoiDung(fullName);
            if (hoaDon.getThanhToan() == 1) {
                dto.setNgayThanhToan(hoaDon.getNgayThanhToan());
            }
            return dto;
        });
    }

    public List<SanPhamChiTietResponse> findTop4BanChay() {
        List<Object[]> dsMa = hoaDonRepository.top4BestSeller();
        List<SanPhamChiTietResponse> dsSpct = new ArrayList<>();
        for (Object[] obj : dsMa) {
            Long maSanPham = (Long) obj[0];
            BigDecimal soLg = (BigDecimal) obj[1];
            Integer soLuong = soLg.intValue();
            System.out.println(maSanPham);
            SanPhamChiTiet spct = sanPhamChiTietService.findById(String.valueOf(maSanPham));
            if (spct != null) {
                SanPhamChiTietResponse sanPhamChiTietResponse = new SanPhamChiTietResponse();
                sanPhamChiTietResponse.setTenSanPham(spct.getSanPham().getTenSanPham() + " " + spct.getMauSac().getTenMau());
                sanPhamChiTietResponse.setSoLuongTon(soLuong);
                sanPhamChiTietResponse.setGiaBan(spct.getGiaBan());
                dsSpct.add(sanPhamChiTietResponse);
            }
        }
        return dsSpct;
    }

    public BigDecimal sumTotalBill() {
        return hoaDonRepository.sumTotalBill();
    }

    public List<NguoiDungResponse> findTop4NguoiMua() {
        List<Object[]> ds = hoaDonRepository.findTop4Buyers();
        List<NguoiDungResponse> dsSpct = new ArrayList<>();
        for (Object[] obj : ds) {
            NguoiDung nguoiDung = (NguoiDung) obj[0];
            Long soLg = (Long) obj[1];
            Integer soLuong = soLg.intValue();
            NguoiDungResponse nguoiDungResponse = new NguoiDungResponse();
            nguoiDungResponse.setTen(nguoiDung.getHo() + " " + nguoiDung.getTenDem() + " " + nguoiDung.getTen());
            nguoiDungResponse.setLuotMua(soLuong);
            nguoiDungResponse.setUsername(nguoiDung.getUsername());
            nguoiDung.setSdt(nguoiDung.getSdt());
            dsSpct.add(nguoiDungResponse);

        }
        return dsSpct;
    }

    public HoaDon orderTaiQuay(HoaDonTaiQuayResquest request, Long maHoaDon) {
        Optional<HoaDon> optional = hoaDonRepository.findById(maHoaDon);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        NguoiDung nguoiDung = nguoiDungRepository.findByUsername(username);
        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository.findByTenPhuongThuc(request.getPhuongThucThanhToan());
        return optional.map(o -> {
//            if (o.getTrangThai() == 0) {

//            o.setNhanVien(nguoiDung.getHo() + " " + nguoiDung.getTenDem() + " " + nguoiDung.getTen());
            o.setNhanVien(nguoiDung.getMaNguoiDung());
            if (request.getMaNguoiDung() != null) {
                o.setNguoiDung(NguoiDung.builder().maNguoiDung(Long.valueOf(request.getMaNguoiDung())).build());
            } else {
                o.setNguoiDung(null);
            }

            o.setThanhToan(request.getThanhToan());
            if (request.getThanhToan() == 1) {
                o.setNgayThanhToan(LocalDateTime.now());
            } else if (request.getThanhToan() == 0) {
                o.setNgayTao(LocalDateTime.now());
            }
            o.setTinh(request.getTinh());
            o.setXa(request.getXa());
            o.setHuyen(request.getHuyen());
            o.setPhuongThucThanhToan(phuongThucThanhToan);
            o.setThanhToan(request.getThanhToan());
            o.setTrangThai(Constants.STATUS_ORDER.HOA_DON_TAI_QUAY);
            o.setXoa(request.getXoa());
            o.setTinh(request.getTinh());
            o.setSdt((request.getSdt()));
            o.setHuyen(request.getHuyen());
            o.setXa(request.getXa());
            o.setDiaChi(request.getDiaChi());
//            } else {
//                return null;
//            }
            return hoaDonRepository.save(o);
        }).orElse(null);
    }

    public Boolean deleteHDCTByAdmin(Long maHDCT) {
        HoaDonChiTiet hoaDonChiTiet = this.hoaDonChiTietRepository.findById(maHDCT).orElse(null);

        HoaDon hoaDon = hoaDonRepository.findById(hoaDonChiTiet.getHoaDon().getMaHoaDon()).orElse(null);
        if(hoaDon.getThanhToan()==1){
            String errorMessage = "Hóa đơn đã thanh toán không thể cập nhập";
            throw new RuntimeException(errorMessage);
        }
        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getHoaDonChiTietList();
        List<VoucherChiTiet> voucherChiTiets = hoaDon.getVoucherChiTiets();
        sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() + hoaDonChiTiet.getSoLuong());
        BigDecimal tongTienSauGiamGia = BigDecimal.ZERO;

//        hoaDonChiTietRepository.deleteById(maHDCT);
        BigDecimal tongTien = BigDecimal.ZERO;
        if (voucherChiTiets != null && !voucherChiTiets.isEmpty()) {
            for (VoucherChiTiet voucherChiTiet : voucherChiTiets) {
                BigDecimal tongTienTruoc = hoaDon.getTongTien().add(hoaDon.getTienGiam());
                BigDecimal giaSauGiam = hoaDonChiTiet.getDonGia().multiply(BigDecimal.valueOf((hoaDonChiTiet.getSoLuong())));
                if (voucherChiTiet.getVoucher().getDieuKien().compareTo(tongTienTruoc.subtract(giaSauGiam)) > 0) {
                    String errorMessage = "Không đạt điều kiện voucher!";
                    throw new RuntimeException(errorMessage);
                }
            }
        }

        hoaDonChiTietRepository.deleteById(maHDCT);

        for (HoaDonChiTiet hoaDonChiTieta : hoaDonChiTiets) {
            sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() + hoaDonChiTieta.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            hoaDonChiTieta.setSoLuong(hoaDonChiTieta.getSoLuong());
            hoaDonChiTietRepository.save(hoaDonChiTieta);
            BigDecimal giaBan = hoaDonChiTieta.getSanPhamChiTiet().getGiaBan()
                    .multiply((BigDecimal.valueOf(100 - hoaDonChiTieta.getSanPhamChiTiet().getPhanTramGiam())).divide(BigDecimal.valueOf(100)));
            tongTien = tongTien.add(giaBan.multiply(BigDecimal.valueOf(hoaDonChiTieta.getSoLuong()))
            );
            sanPhamChiTiet.setSoLuongTon(hoaDonChiTiet.getSanPhamChiTiet().getSoLuongTon() - hoaDonChiTieta.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);
        }

        if (voucherChiTiets != null && !voucherChiTiets.isEmpty()) {
            for (VoucherChiTiet voucherChiTiet : voucherChiTiets) {
                Voucher voucher = voucherChiTiet.getVoucher();
                if (voucher != null) {
                    if (tongTien.compareTo(voucher.getDieuKien()) >= 0) {
                        BigDecimal phanTramGiam = voucher.getGiaTriGiam();
                        BigDecimal giamToiDa = voucher.getGiamToiDa();

                        BigDecimal giamGia = tongTien.multiply(phanTramGiam.divide(BigDecimal.valueOf(100)));

                        if (giamGia.compareTo(giamToiDa) > 0) {
                            giamGia = giamToiDa;
                        }
                        tongTienSauGiamGia = tongTien.subtract(giamGia);
                        hoaDon.setTienGiam(giamGia);
                        hoaDon.setTongTien(tongTienSauGiamGia);


                    } else {
                        String errorMessage = "Không đủ điểu kiện hợp lệ";
                        new MessageResponse(errorMessage);
                        return false;
                    }
                }
            }
        } else {
            hoaDon.setTongTien(tongTien);
        }

        hoaDonChiTiets.remove(hoaDonChiTiet);

        hoaDonRepository.save(hoaDon);

        return true;
    }

    public Boolean suaSoLuongVaoHoaDonKhachHang(Long maHoaDonChiTiet, Integer soLuong, Long maHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);

        HoaDonChiTiet hoaDonChiTiet = this.hoaDonChiTietRepository.findById(maHoaDonChiTiet).orElse(null);
        if (hoaDonChiTiet == null) {
            String errorMessage = "Không tìm thấy mã hóa đơn chi tiết";
            throw new RuntimeException(errorMessage);
        }

        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
        if (sanPhamChiTiet == null) {
            String errorMessage = "Không tìm thấy sản phẩm chi tiết trong hóa đơn chi tiết";
            throw new RuntimeException(errorMessage);
        }



//            List<VoucherChiTiet> voucherChiTiets = hoaDon.getVoucherChiTiets();
//            if (voucherChiTiets != null && !voucherChiTiets.isEmpty()) {
//                for (VoucherChiTiet voucherChiTiet : voucherChiTiets) {
//                    BigDecimal tongTienTruoc = BigDecimal.ZERO;
//                    BigDecimal tienSauGiam = BigDecimal.ZERO;
//                    if (hoaDonChiTiet.getSoLuong() < soLuong) {
//                        tienSauGiam = hoaDonChiTiet.getDonGia().multiply(BigDecimal.valueOf(soLuong - hoaDonChiTiet.getSoLuong()));
//                        tongTienTruoc = hoaDon.getTongTien().add(tienSauGiam).add(hoaDon.getTienGiam());
//                    }
//                    if (hoaDonChiTiet.getSoLuong() > soLuong) {
//                        tienSauGiam = hoaDonChiTiet.getDonGia().multiply(BigDecimal.valueOf(hoaDonChiTiet.getSoLuong() - soLuong));
//                        tongTienTruoc = hoaDon.getTongTien().subtract(tienSauGiam).add(hoaDon.getTienGiam());
//                        if (voucherChiTiet.getVoucher().getDieuKien().compareTo(tongTienTruoc) > 0) {
//                            String errorMessage = "Không đạt điều kiện voucher!";
//                            throw new RuntimeException(errorMessage);
//                            return false;
//                        }
//                    }
//
//                }
//            }

            List<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getHoaDonChiTietList();
            BigDecimal tongTien = BigDecimal.ZERO;
            if (hoaDonChiTiets != null && !hoaDonChiTiets.isEmpty()) {
                for (HoaDonChiTiet hoaDonChiTieta : hoaDonChiTiets) {
                    if (hoaDonChiTiet.getSoLuong() < soLuong) {
                        Integer solgSau = soLuong - hoaDonChiTiet.getSoLuong();
                        sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - solgSau);
                    }
                    if (hoaDonChiTiet.getSoLuong() > soLuong) {
                        Integer solgSau = hoaDonChiTiet.getSoLuong() - soLuong;
                        sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() + solgSau);
                    }
                    sanPhamChiTietRepository.save(sanPhamChiTiet);
                    hoaDonChiTiet.setSoLuong(soLuong);
                    hoaDonChiTietRepository.save(hoaDonChiTiet);
                    BigDecimal giaBan = hoaDonChiTieta.getSanPhamChiTiet().getGiaBan()
                            .multiply((BigDecimal.valueOf(100 - hoaDonChiTieta.getSanPhamChiTiet().getPhanTramGiam())).divide(BigDecimal.valueOf(100)));
                    tongTien = tongTien.add(giaBan.multiply(BigDecimal.valueOf(hoaDonChiTieta.getSoLuong()))
                    );
                }
            }
        BigDecimal tongTienSauGiamGia = BigDecimal.ZERO;
        List<VoucherChiTiet> voucherChiTiets = hoaDon.getVoucherChiTiets();
        if (voucherChiTiets != null && !voucherChiTiets.isEmpty()) {
            for (VoucherChiTiet voucherChiTiet : voucherChiTiets) {
                Voucher voucher = voucherChiTiet.getVoucher();
                if (voucher != null) {
                    if (tongTien.compareTo(voucher.getDieuKien()) >= 0) {
                        BigDecimal phanTramGiam = voucher.getGiaTriGiam();
                        BigDecimal giamToiDa = voucher.getGiamToiDa();
                        BigDecimal giamGia = tongTien.multiply(phanTramGiam.divide(BigDecimal.valueOf(100)));
                        if (giamGia.compareTo(giamToiDa) > 0) {
                            giamGia = giamToiDa;
                        }
                        tongTienSauGiamGia = tongTien.subtract(giamGia);
                        hoaDon.setTienGiam(giamGia);
                        hoaDon.setTongTien(tongTienSauGiamGia);
                    } else {
                        String errorMessage = "Không đủ điều kiện hợp lệ";
                        throw new RuntimeException(errorMessage);

                    }
                } else {
                    hoaDon.setTongTien(tongTien);
                }
            }
        } else {
            hoaDon.setTongTien(tongTien);
        }

            hoaDon.setTongTien(tongTien);
            hoaDon.setVoucherChiTiets(voucherChiTiets);
            hoaDonRepository.save(hoaDon);

//            return hoaDonChiTiet;
           return true;
    }

}
