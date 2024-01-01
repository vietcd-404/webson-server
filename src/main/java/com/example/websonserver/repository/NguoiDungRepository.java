package com.example.websonserver.repository;

import com.example.websonserver.dto.request.UpdateTrangThai;
import com.example.websonserver.dto.response.NguoiDungResponse;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.entity.VaiTroNguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung,Long> {
    public List<NguoiDung> findAllByXoaFalseOrderByNgayTaoDesc();;

    public Page<NguoiDungResponse> findAllByXoaFalse(Pageable pageable);

    List<NguoiDung> findByVaiTro_TenVaiTro(VaiTroNguoiDung vaiTroNguoiDung);

    NguoiDung findByUsername(String username);
    NguoiDung findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsBySdt(String sdt);

    List<NguoiDung> findByTrangThaiAndAndNgayTaoBefore(int trangThai, LocalDateTime cutoffTime);

    @Transactional
    @Modifying
    @Query("UPDATE NguoiDung a " +
            "SET a.xoa = true WHERE a.maNguoiDung = ?1")
    void delete(Long maNguoiDung);

    @Query("SELECT nd FROM NguoiDung nd WHERE nd.username LIKE %:keyword% " +
            "OR nd.ho LIKE %:keyword%")
    List<NguoiDung> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT nd FROM NguoiDung nd WHERE nd.ho LIKE %:keyword% " +
            "OR nd.ten LIKE %:keyword%" +
            "OR nd.tenDem LIKE %:keyword%")
    List<NguoiDung> searchByHoTen(String keyword);


    @Query("SELECT nd FROM NguoiDung nd WHERE nd.ho LIKE %:keyword% " +
            "OR nd.ten LIKE %:keyword%" +
            "OR nd.tenDem LIKE %:keyword%" +
            "OR nd.username LIKE %:keyword%" +
            "OR nd.email LIKE %:keyword%" +
            "OR nd.sdt LIKE %:keyword%" +
            "AND nd.xoa = false " +
            "ORDER BY nd.ngayTao desc")
    public Page<NguoiDung> searchNguoiDung(Pageable pageable,@Param("keyword") String keyword);

}
