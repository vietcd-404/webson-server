package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "dia_chi")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaChi extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_dia_chi")
    private Long maDiaChi;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "loai_dia_chi")
    private String loaiDiaChi;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "tinh")
    private String tinh;

    @Column(name = "huyen")
    private String huyen;

    @Column(name = "xa")
    private String xa;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung nguoiDung;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "xoa")
    private Boolean xoa;
}
