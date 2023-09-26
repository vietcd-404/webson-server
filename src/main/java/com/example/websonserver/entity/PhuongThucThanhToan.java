package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "phuong_thuc_thanh_toan")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhuongThucThanhToan extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_phuong_thuc")
    private Long maPhuongThuc;

    @Column(name = "ten_phuong_thuc")
    private String tenPhuongThuc;

    @Column(name = "trang_thai")
    private int trangThai;

    @Column(name = "xoa")
    private Boolean xoa;
}
