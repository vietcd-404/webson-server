package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "nguoi_dung")
@Entity
@Data
@Builder
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

    @Column(name = "sdt")
    private String sdt;

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

    @ManyToMany
    @JoinTable(name = "nguoi_dung_vai_tro", joinColumns = @JoinColumn(name = "ma_nguoi_dung"),
            inverseJoinColumns = @JoinColumn(name = "ma_vai_tro"))
    private Set<VaiTro> roles = new HashSet<>();
}
