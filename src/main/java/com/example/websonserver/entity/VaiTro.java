package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "vai_tro")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaiTro extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_vai_tro")
    private Long maVaiTro;

    @Column(name = "ten_vai_tro")
    private String tenVaiTro;

    @Column(name = "trang_thai")
    private int trangThai;

    @Column(name = "xoa")
    private Boolean xoa;
}
