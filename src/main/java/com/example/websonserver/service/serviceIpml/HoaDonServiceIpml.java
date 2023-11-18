package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.constants.Constants;
import com.example.websonserver.dto.request.*;
import com.example.websonserver.dto.response.HoaDonChiTietResponse;
import com.example.websonserver.dto.response.HoaDonResponse;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.entity.*;
import com.example.websonserver.exceptions.NotFoundException;
import com.example.websonserver.repository.*;
import com.example.websonserver.service.GioHangCTSessionService;
import com.example.websonserver.service.HoaDonService;
import com.example.websonserver.service.SanPhamChiTietService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

@Service
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
        BigDecimal tongTien = BigDecimal.ZERO;
        for (GioHangChiTiet gioHangChiTiet : gioHang.getCartDetails()) {
            SanPhamChiTiet spct = sanPhamChiTietService.findById(gioHangChiTiet.getSanPhamChiTiet().getMaSanPhamCT().toString());
            HoaDonChiTiet chiTiet1 = new HoaDonChiTiet();
            chiTiet1.setHoaDon(hoaDon);
            chiTiet1.setDonGia(gioHangChiTiet.getDonGia());
            chiTiet1.setSanPhamChiTiet(gioHangChiTiet.getSanPhamChiTiet());
            chiTiet1.setSoLuong(gioHangChiTiet.getSoLuong());
            chiTiet1.setTrangThai(gioHangChiTiet.getTrangThai());
            hoaDonChiTietList.add(chiTiet1);
            tongTien = tongTien.add(gioHangChiTiet.getDonGia().multiply(BigDecimal.valueOf(chiTiet1.getSoLuong())));
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

        List<HoaDonChiTiet> hoaDonChiTietList = new ArrayList<>();


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
            }
        }

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
        return orderResponses;
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
            response.setDiaChiChiTiet(hoaDon.getDiaChi() + ", " + hoaDon.getXa() + ", " + hoaDon.getHuyen() + ", " + hoaDon.getTinh());
            response.setTongTien(hoaDon.getTongTien());
            response.setTenPhuongThucThanhToan(hoaDon.getPhuongThucThanhToan().getTenPhuongThuc());
            response.setHuyen(hoaDon.getHuyen());
            response.setXa(hoaDon.getXa());
            response.setNgayTao(hoaDon.getNgayTao());
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
            for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
                HoaDonChiTietResponse dto = new HoaDonChiTietResponse();
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(hoaDonChiTiet.getSanPhamChiTiet().getMaSanPhamCT()).orElse(null);
                dto.setAnh(anhSanPhamService.getImagesBySanPhamChiTiet(sanPhamChiTiet.getMaSanPhamCT()));
                dto.setTenSanPham(hoaDonChiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham());
                dto.setSoLuong(hoaDonChiTiet.getSoLuong());
                dto.setTenPhuongThucThanhToan(hoaDonChiTiet.getHoaDon().getPhuongThucThanhToan().getTenPhuongThuc());
                dto.setGiaBan(hoaDonChiTiet.getDonGia());
                dto.setSoLuongTon(hoaDonChiTiet.getSanPhamChiTiet().getSoLuongTon());
                dto.setMaHoaDonCT(hoaDonChiTiet.getMaHDCT());
                dto.setMaHoaDon(hoaDon.getMaHoaDon());
                dto.setTrangThai(hoaDon.getTrangThai());
                dto.setMaSanPhamCT(hoaDonChiTiet.getSanPhamChiTiet().getMaSanPhamCT());
                hoaDonChiTietResponses.add(dto);
            }
        }
        return hoaDonChiTietResponses;
    }


    @Override
    public HoaDonChiTiet updateQuantity(Long idSPCT, int soLuong) {

        HoaDonChiTiet hdctNew = new HoaDonChiTiet();
        List<HoaDon> list = this.hoaDonRepository.findByMaHoaDon(hdctNew.getHoaDon().getMaHoaDon());
        for (HoaDon x : list) {
            if (x.getTrangThai() == 0) {
                SanPhamChiTiet spct = sanPhamChiTietRepository.findById(idSPCT).orElse(null);
//                HoaDonChiTiet hdct = hoaDonChiTietRepository.findByMaSPCTAndMaHD(idSPCT, x.getMaHoaDon());
                //Lấy ra số lượng trước update
                int soLuongCu = hdctNew.getSoLuong();
                //Cộng số lượng cũ vào số lượng sản phẩm chi tiết
                spct.setSoLuongTon(spct.getSoLuongTon() + soLuongCu);
                //Lưu
                sanPhamChiTietRepository.save(spct);
                //Set số lượng mới
                hdctNew.setSoLuong(soLuong);
                //Lưu
                hoaDonChiTietRepository.save(hdctNew);
                //Trừ số lượng trong sản phẩm chi tiết
                spct.setSoLuongTon(spct.getSoLuongTon() - soLuong);
                //Lưu
                sanPhamChiTietRepository.save(spct);
            } else {
                String errorMessage = "Đơn hàng đã được xác nhận,bạn có thể hủy và đặt lại.";
                throw new RuntimeException(errorMessage);
            }
        }
        return hdctNew;
    }

    @Override
    public String HuyHoaDon(Long maHD) {
        HoaDon hd = hoaDonRepository.findById(maHD).orElse(null);
        if (hd.getTrangThai() == 0) {
            hd.setTrangThai(Constants.STATUS_ORDER.DA_HUY);
            hoaDonRepository.save(hd);
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
//
//
//                    }
//                    o.setTongTien(tongTienSauGiamGia);
//                }else {
//                    o.setTongTien(tongTien);
//                }
//
//                o.setVoucherChiTiets(voucherChiTiets);

            } else {
                return null;
            }
            return hoaDonRepository.save(o);
        }).orElse(null);
    }

    public void deleteHDCT(Long maHDCT) {
        BigDecimal tongTien = BigDecimal.ZERO;
        HoaDonChiTiet hoaDonChiTiet = this.hoaDonChiTietRepository.findById(maHDCT).orElse(null);
        HoaDon hoaDon = hoaDonChiTiet.getHoaDon();
        hoaDonChiTietRepository.deleteById(maHDCT);
    }

    public HoaDonChiTiet suaSoLuongVaoHoaDon(Long maHoaDonChiTiet, Integer soLuong ,Long maHoaDon) {
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
        if (soLuong > soLuongTon) {
            String errorMessage = "Số lượng cập nhật vượt quá số lượng tồn kho";
            throw new RuntimeException(errorMessage);
        }



        List<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getHoaDonChiTietList();
                BigDecimal tongTien = BigDecimal.ZERO;
                if (hoaDonChiTiets != null && !hoaDonChiTiets.isEmpty()) {
                    for (HoaDonChiTiet hoaDonChiTieta : hoaDonChiTiets) {
                        sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() + hoaDonChiTiet.getSoLuong());
                        sanPhamChiTietRepository.save(sanPhamChiTiet);
                        hoaDonChiTiet.setSoLuong(soLuong);
                        hoaDonChiTietRepository.save(hoaDonChiTiet);
                        tongTien = tongTien.add(hoaDonChiTieta.getDonGia().multiply(BigDecimal.valueOf(hoaDonChiTieta.getSoLuong())));
                        sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - soLuong);
                        sanPhamChiTietRepository.save(sanPhamChiTiet);
                    }
                }
                BigDecimal tongTienSauGiamGia = BigDecimal.ZERO;
                List<VoucherChiTiet> voucherChiTiets = hoaDon.getVoucherChiTiets();

                if (voucherChiTiets != null && !voucherChiTiets.isEmpty()) {
                    for (VoucherChiTiet voucherChiTiet : voucherChiTiets) {
                        Voucher voucher = voucherChiTiet.getVoucher();
                        if(voucher!=null){
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

                            }else {
                                String errorMessage = "Không đủ điểu kiện hợp lệ";
                                throw new RuntimeException(errorMessage);
                            }
                        }else {
                            hoaDon.setTongTien(tongTien);
                        }
                    }
                }else {
                    hoaDon.setTongTien(tongTien);
                }

                hoaDon.setVoucherChiTiets(voucherChiTiets);
                hoaDonRepository.save(hoaDon);

        return hoaDonChiTiet;
    }
}
