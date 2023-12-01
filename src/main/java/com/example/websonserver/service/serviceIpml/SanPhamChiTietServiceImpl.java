package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.SanPhamChiTietRequest;
import com.example.websonserver.dto.request.SanPhamChiTietRequestDemo;
import com.example.websonserver.dto.request.ThuocTinhRequest;
import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.dto.response.SanPhamChiTietRes;
import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.dto.response.SanPhamTheoThuongHieuResponse;
import com.example.websonserver.entity.*;
import com.example.websonserver.repository.AnhSanPhamRepository;
import com.example.websonserver.repository.HoaDonChiTietRepository;
import com.example.websonserver.repository.SanPhamChiTietRepository;
import com.example.websonserver.repository.SanPhamRepository;
import com.example.websonserver.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SanPhamChiTietServiceImpl implements SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private AnhSanPhamRepository anhSanPhamRepository;

    @Autowired
    private AnhSanPhamServiceImpl anhSanPhamService;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private LoaiService loaiService;

    @Autowired
    private ThuongHieuService thuongHieuService;

    @Autowired
    private MauSacService mauSacService;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Override
    public SanPhamChiTiet createOne(SanPhamChiTietRequest request) {
        SanPham getSPByTenSP = sanPhamService.findByTen(request.getTenSanPham());
        SanPhamChiTiet spct = new SanPhamChiTiet();

        spct.setGiaBan(request.getGiaBan());
        spct.setPhanTramGiam(request.getPhanTramGiam());
        spct.setSoLuongTon(request.getSoLuongTon());
        spct.setSanPham(getSPByTenSP);
        spct.setLoai(loaiService.findByTen(request.getTenLoai()));
        spct.setThuongHieu(thuongHieuService.findByTen(request.getTenThuongHieu()));
        spct.setMauSac(mauSacService.findByTen(request.getTenMau()));
        spct.setTrangThai(request.getTrangThai());
        spct.setXoa(request.getXoa());

        List<AnhSanPham> danhSachAnh = new ArrayList<>();
        List<Long> danhSachAnhIds = request.getDanhSachAnh();
        if (danhSachAnhIds != null && !danhSachAnhIds.isEmpty()) {
            danhSachAnh = anhSanPhamRepository.findAllById(danhSachAnhIds);
        }
        spct.setAnhSanPhamList(danhSachAnh);
        return sanPhamChiTietRepository.save(spct);
    }

    @Override
    public List<SanPhamChiTiet> createList(List<SanPhamChiTietRequest> listRequest) {
        List<SanPhamChiTiet> spctList = new ArrayList<>();
        for (SanPhamChiTietRequest x : listRequest) {
            SanPham getSPByTenSP = sanPhamService.findByTen(x.getTenSanPham());
            SanPhamChiTiet spct = null;
            if (getSPByTenSP != null) {
                spct = SanPhamChiTiet.builder()
                        .giaBan(x.getGiaBan())
                        .phanTramGiam(x.getPhanTramGiam())
                        .soLuongTon(x.getSoLuongTon())
                        .sanPham(getSPByTenSP)
                        .loai(loaiService.findByTen(x.getTenLoai()))
                        .thuongHieu(thuongHieuService.findByTen(x.getTenThuongHieu()))
                        .mauSac(mauSacService.findByTen(x.getTenMau()))
//                        .anhSanPhamList(request.getAnhSanPhamList())
                        .trangThai(x.getTrangThai())
                        .xoa(x.getXoa())
                        .build();
            }
            spctList.add(spct);
        }
        return sanPhamChiTietRepository.saveAll(spctList);
    }


    @Override
    public SanPhamChiTiet update(SanPhamChiTietRequest request, Long id) {
        Optional<SanPhamChiTiet> optional = sanPhamChiTietRepository.findById(id);
        return optional.map(o -> {
            o.setGiaBan(request.getGiaBan());
            o.setPhanTramGiam(request.getPhanTramGiam());
            o.setSoLuongTon((request.getSoLuongTon()));
            o.setSanPham(sanPhamService.findByTen(request.getTenSanPham()));
            o.setLoai(loaiService.findByTen(request.getTenLoai()));
            o.setThuongHieu(thuongHieuService.findByTen(request.getTenThuongHieu()));
            o.setMauSac(mauSacService.findByTen(request.getTenMau()));
            return sanPhamChiTietRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<SanPhamChiTiet> getAll(Pageable pageable) {
        return sanPhamChiTietRepository.findAllByXoaFalse(pageable);
    }

    @Override
    public List<SanPhamChiTietResponse> getAllCT() {
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietRepository.findAllByXoaFalseOrderByNgayTaoDesc();
        List<SanPhamChiTietResponse> sanPhamChiTietDtos = new ArrayList<>();
        for (SanPhamChiTiet sanPhamChiTiet : sanPhamChiTietList) {
            SanPhamChiTietResponse dto = new SanPhamChiTietResponse();
            dto.setMaSanPhamCT(sanPhamChiTiet.getMaSanPhamCT());
            dto.setGiaBan(sanPhamChiTiet.getGiaBan());
            dto.setPhanTramGiam(sanPhamChiTiet.getPhanTramGiam());
            dto.setSoLuongTon(sanPhamChiTiet.getSoLuongTon());
            SanPham sanPham = sanPhamChiTiet.getSanPham();
            if (sanPham != null) {
                String tenSanPham = sanPham.getTenSanPham();
                dto.setTenSanPham(tenSanPham);
            }
            Loai loai = sanPhamChiTiet.getLoai();
            if (loai != null) {
                String tenLoai = loai.getTenLoai();
                dto.setTenLoai(tenLoai);
            }

            ThuongHieu thuongHieu = sanPhamChiTiet.getThuongHieu();
            if (thuongHieu != null) {
                String tenthuongHieu = thuongHieu.getTenThuongHieu();
                dto.setTenThuongHieu(tenthuongHieu);
            }
            MauSac mauSac = sanPhamChiTiet.getMauSac();
            if (mauSac != null) {
                String tenMau = mauSac.getTenMau();
                dto.setTenMau(tenMau);
            }
            List<AnhSanPham> imageUrls = anhSanPhamService.getImage(sanPhamChiTiet.getMaSanPhamCT());
            dto.setDanhSachAnh(imageUrls);
            dto.setTrangThai(sanPhamChiTiet.getTrangThai());
            sanPhamChiTietDtos.add(dto);
        }

        return sanPhamChiTietDtos;
    }

    @Override
    public void delete(Long id) {
        sanPhamChiTietRepository.delete(id);
    }

    @Override
    public SanPhamChiTiet findById(String id) {
        Optional<SanPhamChiTiet> anhsp = sanPhamChiTietRepository.findById(Long.parseLong(id));
        SanPhamChiTiet spct = anhsp.orElse(null);
        return spct;
    }

    @Override
    public SanPhamChiTietResponse findByIdResponse(String id) {
        Optional<SanPhamChiTiet> anhsp = sanPhamChiTietRepository.findById(Long.parseLong(id));
        SanPhamChiTiet spct = anhsp.orElse(null);
        List<AnhSanPham> imageUrls = anhSanPhamService.getImage(spct.getMaSanPhamCT());
        SanPhamChiTietResponse res = SanPhamChiTietResponse.builder()
                .maSanPhamCT(spct.getMaSanPhamCT())
                .img(anhSanPhamService.getImagesBySanPhamChiTiet(spct.getMaSanPhamCT()))
                .giaBan(spct.getGiaBan())
                .danhSachAnh(imageUrls)
                .tenSanPham(spct.getSanPham().getTenSanPham())
                .phanTramGiam(spct.getPhanTramGiam())
                .soLuongTon(spct.getSoLuongTon())
                .tenLoai(spct.getLoai().getTenLoai())
                .tenMau(spct.getMauSac().getTenMau())
                .tenThuongHieu(spct.getThuongHieu().getTenThuongHieu())
                .build();
        return res;
    }

    @Override
    public Page<SanPhamChiTietRes> getAllSanPham(Long maSanPham, Long maLoai, Long maThuongHieu, Long maMau, int page, int size, BigDecimal giaThap, BigDecimal giaCao, String sortBy, String sortDirection) {
        return null;
    }

    @Override
    public SanPhamChiTiet updateStatus(UpdateTrangThai request, Long id) {
        Optional<SanPhamChiTiet> optional = sanPhamChiTietRepository.findById(id);
        return optional.map(o -> {
            o.setTrangThai(request.getTrangThai());
            return sanPhamChiTietRepository.save(o);
        }).orElse(null);
    }

    @Override
    public List<SanPhamChiTietRes> Top5SanPhamMoiNhat() {
        List<SanPhamChiTietRes> sanPhamChiTietPage = sanPhamChiTietRepository.Top5SanPhamMoiNhat().stream()
                .map(sanPhamChiTiets -> new SanPhamChiTietRes(
                        sanPhamChiTiets.getMaSanPhamCT(),
                        sanPhamChiTiets.getGiaBan(),
                        sanPhamChiTiets.getPhanTramGiam(),
                        sanPhamChiTiets.getSoLuongTon(),
                        sanPhamChiTiets.getSanPham().getTenSanPham(),
                        sanPhamChiTiets.getLoai().getTenLoai(),
                        sanPhamChiTiets.getThuongHieu().getTenThuongHieu(),
                        sanPhamChiTiets.getMauSac().getTenMau(),
                        sanPhamChiTiets.getTrangThai(),
                        anhSanPhamService.getImagesBySanPhamChiTiet(sanPhamChiTiets.getMaSanPhamCT())
                )).collect(Collectors.toList());
        return sanPhamChiTietPage;
    }


    public Page<SanPhamChiTietRes> getAllSanPhamUser(ThuocTinhRequest request,
                                                     int page, int size) {
        Long maLoai = request.getMaLoai();
        Long maSanPham = request.getMaSanPham();
        Long maMau = request.getMaMau();
        Long maThuongHieu = request.getMaThuongHieu();
        Integer trangThai = request.getTrangThai();
        BigDecimal giaCao = request.getGiaCao();
        BigDecimal giaThap = request.getGiaThap();
        Boolean giaTangDan = request.getGiaTangDan();
        Boolean giaGiamDan = request.getGiaGiamDan();
        String tenSanPham = request.getTenSanPham();


        Specification<SanPhamChiTiet> receptionistSpecification = (root, cq, cb) -> {
            Predicate predicate = cb.conjunction();

            if (tenSanPham != null && !tenSanPham.isEmpty()) {
                predicate = cb.and(predicate, cb.like(root.get("sanPham").get("tenSanPham"), "%" + tenSanPham + "%"));
            }
            if (maLoai != null) {
                predicate = cb.and(predicate, cb.equal(root.get("loai").get("maLoai"), maLoai));
            }
            if (maSanPham != null) {
                predicate = cb.and(predicate, cb.equal(root.get("sanPham").get("maSanPham"), maSanPham));
            }
            if (maMau != null) {
                predicate = cb.and(predicate, cb.equal(root.get("mauSac").get("maMau"), maMau));
            }
            if (maThuongHieu != null) {
                predicate = cb.and(predicate, cb.equal(root.get("thuongHieu").get("maThuongHieu"), maThuongHieu));
            }
            if (giaThap != null && giaCao != null) {
                predicate = cb.and(predicate, cb.between(root.get("giaBan"), giaCao, giaThap));
            }
            predicate = cb.and(predicate, cb.equal(root.get("trangThai"), 1));
            predicate = cb.and(predicate, cb.equal(root.get("xoa"), false));
            return predicate;
        };

        Sort sort = null;
         sort = Sort.by(Sort.Direction.DESC, "ngayTao");
        if (giaGiamDan != null && giaGiamDan) {
            sort = Sort.by(Sort.Direction.DESC, "giaBan");
        } else if (giaTangDan != null && giaTangDan) {
            sort = Sort.by(Sort.Direction.ASC, "giaBan");
        }

        Pageable pageable = PageRequest.of(page - 1, size,sort);
        Page<SanPhamChiTietRes> sanPhamChiTietPage = sanPhamChiTietRepository
                .findAll(receptionistSpecification, pageable)
                .map(sanPhamChiTiets -> new SanPhamChiTietRes(
                                        sanPhamChiTiets.getMaSanPhamCT(),
                                        sanPhamChiTiets.getGiaBan(),
                                        sanPhamChiTiets.getPhanTramGiam(),
                                        sanPhamChiTiets.getSoLuongTon(),
                                        sanPhamChiTiets.getSanPham().getTenSanPham(),
                                        sanPhamChiTiets.getLoai().getTenLoai(),
                                        sanPhamChiTiets.getThuongHieu().getTenThuongHieu(),
                                        sanPhamChiTiets.getMauSac().getTenMau(),
                                        sanPhamChiTiets.getTrangThai(),
                                        anhSanPhamService.getImagesBySanPhamChiTiet(sanPhamChiTiets.getMaSanPhamCT())
                                ));

        return sanPhamChiTietPage;
    }
    public List<SanPhamChiTietResponse> sanPhamDetail(Long maSanPhamCT) {
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietRepository.findAllByXoaFalseAndTrangThaiAndMaSanPhamCT(1,maSanPhamCT);
        List<SanPhamChiTietResponse> sanPhamChiTietDtos = new ArrayList<>();
        for (SanPhamChiTiet sanPhamChiTiet : sanPhamChiTietList) {

            SanPhamChiTietResponse dto = new SanPhamChiTietResponse();
            dto.setMaSanPhamCT(sanPhamChiTiet.getMaSanPhamCT());
            dto.setGiaBan(sanPhamChiTiet.getGiaBan());
            dto.setPhanTramGiam(sanPhamChiTiet.getPhanTramGiam());
            dto.setSoLuongTon(sanPhamChiTiet.getSoLuongTon());
            SanPham sanPham = sanPhamChiTiet.getSanPham();
            if (sanPham != null) {
                String tenSanPham = sanPham.getTenSanPham();
                dto.setTenSanPham(tenSanPham);
            }
            Loai loai = sanPhamChiTiet.getLoai();
            if (loai != null) {
                String tenLoai = loai.getTenLoai();
                dto.setTenLoai(tenLoai);
            }

            ThuongHieu thuongHieu = sanPhamChiTiet.getThuongHieu();
            if (thuongHieu != null) {
                String tenthuongHieu = thuongHieu.getTenThuongHieu();
                dto.setTenThuongHieu(tenthuongHieu);
            }
            MauSac mauSac = sanPhamChiTiet.getMauSac();
            if (mauSac != null) {
                String tenMau = mauSac.getTenMau();
                dto.setTenMau(tenMau);
            }
            List<AnhSanPham> imageUrls = anhSanPhamService.getImage(sanPhamChiTiet.getMaSanPhamCT());
            dto.setDanhSachAnh(imageUrls);
            dto.setImg(anhSanPhamService.getImagesBySanPhamChiTiet(sanPhamChiTiet.getMaSanPhamCT()));
            dto.setTrangThai(sanPhamChiTiet.getTrangThai());
            List<SanPhamTheoThuongHieuResponse> sanPhamChiTietThuongHieu = new ArrayList<>();
            List<SanPhamChiTiet> sanPhamChiTietList1=sanPhamChiTietRepository.findByThuongHieu_TenThuongHieuAndXoaFalseAndTrangThai(sanPhamChiTiet.getThuongHieu().getTenThuongHieu(),1);
            for (SanPhamChiTiet sanPhamChiTiet1 : sanPhamChiTietList1){
                SanPhamTheoThuongHieuResponse response = new SanPhamTheoThuongHieuResponse();
                response.setMaSanPhamCT(sanPhamChiTiet1.getMaSanPhamCT());
                response.setGiaBan(sanPhamChiTiet1.getGiaBan());
                response.setPhanTramGiam(sanPhamChiTiet1.getPhanTramGiam());
                response.setSoLuongTon(sanPhamChiTiet1.getSoLuongTon());
                SanPham sanPham1 = sanPhamChiTiet1.getSanPham();
                if (sanPham != null) {
                    String tenSanPham = sanPham1.getTenSanPham();
                    response.setTenSanPham(tenSanPham);
                }
                Loai loai1 = sanPhamChiTiet1.getLoai();
                if (loai != null) {
                    String tenLoai = loai1.getTenLoai();
                    response.setTenLoai(tenLoai);
                }

                ThuongHieu thuongHieu1 = sanPhamChiTiet1.getThuongHieu();
                if (thuongHieu != null) {
                    String tenthuongHieu = thuongHieu1.getTenThuongHieu();
                    response.setTenThuongHieu(tenthuongHieu);
                }
                MauSac mauSac1 = sanPhamChiTiet1.getMauSac();
                if (mauSac != null) {
                    String tenMau = mauSac1.getTenMau();
                    response.setTenMau(tenMau);
                }
                List<AnhSanPham> imageUrlsa = anhSanPhamService.getImage(sanPhamChiTiet1.getMaSanPhamCT());
                response.setDanhSachAnh(imageUrlsa);
                response.setImg(anhSanPhamService.getImagesBySanPhamChiTiet(sanPhamChiTiet1.getMaSanPhamCT()));
                response.setTrangThai(sanPhamChiTiet1.getTrangThai());
                sanPhamChiTietThuongHieu.add(response);
            }
            dto.setThuongHieuList(sanPhamChiTietThuongHieu);

            sanPhamChiTietDtos.add(dto);
        }

        return sanPhamChiTietDtos;
    }

    @Override
    public List<SanPhamChiTietResponse> getAllLoc() {
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietRepository.findAllByXoaFalseAndTrangThai(1);
        List<SanPhamChiTietResponse> sanPhamChiTietDtos = new ArrayList<>();
        for (SanPhamChiTiet sanPhamChiTiet : sanPhamChiTietList) {
            SanPhamChiTietResponse dto = new SanPhamChiTietResponse();
            dto.setMaSanPhamCT(sanPhamChiTiet.getMaSanPhamCT());
            dto.setGiaBan(sanPhamChiTiet.getGiaBan());
            dto.setPhanTramGiam(sanPhamChiTiet.getPhanTramGiam());
            dto.setSoLuongTon(sanPhamChiTiet.getSoLuongTon());
            SanPham sanPham = sanPhamChiTiet.getSanPham();
            if (sanPham != null) {
                String tenSanPham = sanPham.getTenSanPham();
                dto.setTenSanPham(tenSanPham);
            }
            Loai loai = sanPhamChiTiet.getLoai();
            if (loai != null) {
                String tenLoai = loai.getTenLoai();
                dto.setTenLoai(tenLoai);
            }

            ThuongHieu thuongHieu = sanPhamChiTiet.getThuongHieu();
            if (thuongHieu != null) {
                String tenthuongHieu = thuongHieu.getTenThuongHieu();
                dto.setTenThuongHieu(tenthuongHieu);
            }
            MauSac mauSac = sanPhamChiTiet.getMauSac();
            if (mauSac != null) {
                String tenMau = mauSac.getTenMau();
                dto.setTenMau(tenMau);
            }
            dto.setImg(anhSanPhamService.getImagesBySanPhamChiTiet(sanPhamChiTiet.getMaSanPhamCT()));
            dto.setTrangThai(sanPhamChiTiet.getTrangThai());
            sanPhamChiTietDtos.add(dto);
        }

        return sanPhamChiTietDtos;
    }

    @Override
    public List<SanPhamChiTietResponse> findTop4BanChay() {
        List<Object[]> lst = hoaDonChiTietRepository.findTop4BanChay();
        List<SanPhamChiTietResponse> dtoList = new ArrayList<>();
        for (Object[] x : lst) {
            Long maSanPhamChiTiet = Long.parseLong(x[0].toString());
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(maSanPhamChiTiet).orElse(null);
            if (spct != null) {
                List<AnhSanPham> imageUrls = anhSanPhamService.getImage(spct.getMaSanPhamCT());
                SanPhamChiTietResponse res = SanPhamChiTietResponse.builder()
                        .maSanPhamCT(spct.getMaSanPhamCT())
                        .img(anhSanPhamService.getImagesBySanPhamChiTiet(spct.getMaSanPhamCT()))
                        .giaBan(spct.getGiaBan())
                        .danhSachAnh(imageUrls)
                        .tenSanPham(spct.getSanPham().getTenSanPham())
                        .phanTramGiam(spct.getPhanTramGiam())
                        .soLuongTon(spct.getSoLuongTon())
                        .tenLoai(spct.getLoai().getTenLoai())
                        .tenMau(spct.getMauSac().getTenMau())
                        .tenThuongHieu(spct.getThuongHieu().getTenThuongHieu())
                        .build();
                dtoList.add(res);
            }
        }
        return dtoList;
    }

    @Override
    public SanPhamChiTiet findDuplicate(String tenSanPham, String tenLoai, String tenMau, String tenThuongHieu) {
        return sanPhamChiTietRepository.findByNamesAndXoaFalse(tenSanPham,tenLoai,tenMau,tenThuongHieu);
    }

    public void addImagesToProductChiTiet(Long productId, List<Long> imageIds) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm chi tiết với mã: " + productId));

        List<AnhSanPham> images = anhSanPhamRepository.findAllById(imageIds);

        // Liên kết các ảnh với sản phẩm chi tiết
        for (AnhSanPham image : images) {
            image.setSanPhamChiTiet(sanPhamChiTiet);
            image.setTrangThai(1);
        }

        // Lưu danh sách ảnh vào cơ sở dữ liệu
        anhSanPhamRepository.saveAll(images);
    }

    public void themNhieuAnhChoSanPham(Long sanPhamId, List<Long> danhSachIdAnh) {
        SanPhamChiTiet sanPham = sanPhamChiTietRepository.findById(sanPhamId).orElse(null);
        if (sanPham != null) {
            for (Long anhId : danhSachIdAnh) {
                AnhSanPham anh = anhSanPhamRepository.findById(anhId).orElse(null);
                if (anh != null) {
                    sanPham.getAnhSanPhamList().add(anh);
                }
            }
            sanPhamChiTietRepository.save(sanPham);

        } else {

        }
    }

    public SanPhamChiTiet layAnh(Long anhId) {
        return sanPhamChiTietRepository.findById(anhId).orElse(null);
    }

    public List<SanPhamChiTietResponse> getSanPhamByThuongHieu(String tenThuongHieu) {
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietRepository.findByThuongHieu_TenThuongHieuAndXoaFalseAndTrangThai(tenThuongHieu, 1);
        List<SanPhamChiTietResponse> sanPhamChiTietDtos = new ArrayList<>();
        int maxResults = 5;
        for (SanPhamChiTiet sanPhamChiTiet : sanPhamChiTietList) {
            if (sanPhamChiTietDtos.size() >= maxResults) {
                break;
            }
            SanPhamChiTietResponse dto = new SanPhamChiTietResponse();
            dto.setMaSanPhamCT(sanPhamChiTiet.getMaSanPhamCT());
            dto.setGiaBan(sanPhamChiTiet.getGiaBan());
            SanPham sanPham = sanPhamChiTiet.getSanPham();
            if (sanPham != null) {
                String tenSanPham = sanPham.getTenSanPham();
                dto.setTenSanPham(tenSanPham);
            }
            Loai loai = sanPhamChiTiet.getLoai();
            if (loai != null) {
                String tenLoai = loai.getTenLoai();
                dto.setTenLoai(tenLoai);
            }

            ThuongHieu thuongHieu = sanPhamChiTiet.getThuongHieu();
            if (thuongHieu != null) {
                String tenthuongHieu = thuongHieu.getTenThuongHieu();
                dto.setTenThuongHieu(tenthuongHieu);
            }
            MauSac mauSac = sanPhamChiTiet.getMauSac();
            if (mauSac != null) {
                String tenMau = mauSac.getTenMau();
                dto.setTenMau(tenMau);
            }
            dto.setPhanTramGiam(sanPhamChiTiet.getPhanTramGiam());
            dto.setImg(anhSanPhamService.getImagesBySanPhamChiTiet(sanPhamChiTiet.getMaSanPhamCT()));
            dto.setTrangThai(sanPhamChiTiet.getTrangThai());
            sanPhamChiTietDtos.add(dto);
        }

        return sanPhamChiTietDtos;
    }
}
