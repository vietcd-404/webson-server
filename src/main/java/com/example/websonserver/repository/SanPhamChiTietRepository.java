package com.example.websonserver.repository;

import com.example.websonserver.dto.response.SanPhamChiTietResponse;
import com.example.websonserver.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Long> , JpaSpecificationExecutor<SanPhamChiTiet> {
    public Page<SanPhamChiTiet> findAllByXoaFalse(Pageable pageable);

    public List<SanPhamChiTiet> findAllByXoaFalseAndTrangThai(Integer trangThai);
    public List<SanPhamChiTiet> findAllByXoaFalse();

    @Query("SELECT s FROM SanPhamChiTiet s " +
            "JOIN s.anhSanPhamList a " +
            "WHERE s.maSanPhamCT = :maSanPhamCT")
    List<SanPhamChiTiet> findSanPhamChiTietWithImages(@Param("maSanPhamCT") Long maSanPhamCT);


    @Query(value = "select san_pham.ten_san_pham as tenSanPham,\n" +
            "loai.ten_loai as tenLoai,\n" +
            "thuong_hieu.ten_thuong_hieu as tenThuongHieu, \n" +
            "mau.ten_mau as tenMau,\n" +
            "san_pham_chi_tiet.ma_san_pham_chi_tiet as maSanPhamCT,\n" +
            "san_pham_chi_tiet.gia_ban  as giaBan,\n" +
            "san_pham_chi_tiet.phan_tram_giam as phanTramGiam,\n" +
            "san_pham_chi_tiet.so_luong_ton  as soLuongTon,\n" +
            "san_pham_chi_tiet.trang_thai as trangThai,\n" +
            "anh_san_pham_chi_tiet.anh AS danhSachAnh\n" +
            "from san_pham_chi_tiet  \n" +
            "join mau on mau.ma_mau = san_pham_chi_tiet.ma_mau \n" +
            "join thuong_hieu on thuong_hieu.ma_thuong_hieu  = san_pham_chi_tiet.ma_thuong_hieu \n" +
            "join san_pham on san_pham.ma_san_pham = san_pham_chi_tiet.ma_san_pham \n" +
            "join loai on loai.ma_loai = san_pham_chi_tiet.ma_loai \n" +
            "join anh_san_pham_chi_tiet on anh_san_pham_chi_tiet.ma_anh = san_pham_chi_tiet.ma_san_pham_chi_tiet\n" +
            "where anh_san_pham_chi_tiet.xoa =0 and san_pham_chi_tiet.xoa =0\n"
            , nativeQuery = true)
    Page<SanPhamChiTiet> getAllCT(Pageable pageable);


    @Transactional
    @Modifying
    @Query("UPDATE SanPhamChiTiet a " +
            "SET a.xoa = true WHERE a.maSanPhamCT = ?1")
    void delete(Long maSanPhamCT);
}
