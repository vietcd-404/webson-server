package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.dto.request.AnhSanPhamRequest;
import com.example.websonserver.entity.AnhSanPham;
import com.example.websonserver.entity.SanPhamChiTiet;
import com.example.websonserver.repository.AnhSanPhamRepository;
import com.example.websonserver.service.AnhSanPhamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnhSanPhamServiceImpl implements AnhSanPhamService {
    @Autowired
    private AnhSanPhamRepository anhSanPhamRepository;
    @Override
    public AnhSanPham create(AnhSanPhamRequest anhSP, MultipartFile data) {
        anhSP.setAnh(data.getOriginalFilename());
        AnhSanPham anhSanPham = anhSP.map(new AnhSanPham());
        return anhSanPhamRepository.save(anhSanPham);
    }

    @Override
    public AnhSanPham update(AnhSanPhamRequest anhSP, Long id) {
        Optional<AnhSanPham> optional = anhSanPhamRepository.findById(id);
        return optional.map(o->{
//            o.setAnh(anhSP.getAnh());
            o.setTrangThai(anhSP.getTrangThai());
            o.setXoa(anhSP.getXoa());
            return anhSanPhamRepository.save(o);
        }).orElse(null);
    }

    @Override
    public Page<AnhSanPham> getAll(Pageable pageable) {

        return anhSanPhamRepository.getAll(pageable);
    }

    public Page<AnhSanPham> getAllAnh(Pageable pageable) {
        return anhSanPhamRepository.findAllByXoaFalse(pageable);
    }

    @Override
    @Transient
    public void delete(Long id) {
        anhSanPhamRepository.delete(id);
    }


    @Transient
    public void deleteAnh(Long id) {
        anhSanPhamRepository.deleteAnh(id);
    }

    public AnhSanPham layAnhThem(Long anhId) {
        return anhSanPhamRepository.findById(anhId).orElse(null);
    }


    @Override
    public AnhSanPham findById(String id) {
        Optional<AnhSanPham>anhsp = anhSanPhamRepository.findById(Long.parseLong(id));
        AnhSanPham anhSanPham = anhsp.orElse(null);
        return anhSanPham;
    }


    @Override
    public List<AnhSanPham> getImagesBySanPhamChiTiet(Long maSanPhamCT) {
        List<AnhSanPham> imageUrls = anhSanPhamRepository.findImageUrlsBySanPhamChiTietId(maSanPhamCT);
        return imageUrls;
    }



//    public String uploadImage(MultipartFile file) throws IOException {
//
//        AnhSanPham imageData = anhSanPhamRepository.save(AnhSanPham.builder()
//
//                .anh(AnhSanPhamUtil.compressImage(file.getBytes())).build());
//        if (imageData != null) {
//            return "file uploaded successfully : " + file.getOriginalFilename();
//        }
//        return null;
//    }

    public List<AnhSanPham> getAllAnh(){
        return anhSanPhamRepository.findAll();
    }
    public AnhSanPham uploadAnh(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Tệp rỗng");
        }

        try {
            byte[] bytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);

            AnhSanPham anh = new AnhSanPham();
            anh.setAnh(base64Image);
            anh.setTrangThai(0);
            anh.setXoa(false);
            return anhSanPhamRepository.save(anh);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi xử lý tệp ảnh", e);
        }
    }

//    public byte[] downloadImage(Long maAnh){
//        Optional<AnhSanPham> dbImageData = anhSanPhamRepository.findById(maAnh);
//        byte[] images=AnhSanPhamUtil.decompressImage(dbImageData.get().getAnh());
//        return images;
//    }

    public List<Long> getListOfImageIds() {
        List<AnhSanPham> anhSanPhams = anhSanPhamRepository.findAll(); // Lấy danh sách tất cả các ảnh
        List<Long> imageIds = new ArrayList<>();

        for (AnhSanPham anhSanPham : anhSanPhams) {
             imageIds.add(anhSanPham.getMaAnh());
        }

        return imageIds;
    }







}
