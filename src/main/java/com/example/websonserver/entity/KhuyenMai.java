package com.example.websonserver.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Table(name = "khuyen_mai")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KhuyenMai extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_khuyen_mai")
    private Long maKhuyenMai;

    @Column(name = "dieu_kien")
    private BigDecimal dieuKien;

    @Column(name = "gia_tri_giam")
    private BigDecimal giaTriGiam;

    @Column(name = "giam_toi_da")
    private BigDecimal giamToiDa;

    @Column(name = "kieu_giam_gia")
    private String kieuGiamGia;

    @Column(name = "mo_ta", length = 10000)
    private String moTa;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "khuyen_mai")
    private String khuyenMai;


    @Column(name = "thoi_gian_bat_dau")
    @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime thoiGianBatDau;


    @Column(name = "thoi_gian_ket_thuc")
    @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime thoiGianKetThuc;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "xoa")
    private Boolean xoa;

}
