package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "nguoi_dung_vai_tro")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaiTroNguoiDung extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nguoi_dung_vai_tro")
    private Long ma;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung maNguoiDung;

    @ManyToOne
    @JoinColumn(name = "ma_vai_tro")
    private VaiTro maVaiTro;


}
