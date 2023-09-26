package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name = "nguoi_dung")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NguoiDung extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nguoi_dung")
    private Long maNguoiDung;

    @Column(name = "email")
    private String email;

    @Column(name = "gioi_tinh")
    private Integer gioiTinh;

    @Column(name = "anh")
    private String anh;

    @Column(name = "ngay_sinh")
    private Date ngaySinh;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "ho")
    private String ho;

    @Column(name = "ten_dem")
    private String tenDem;

    @Column(name = "ten")
    private String ten;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "xoa")
    private Boolean xoa;
}
