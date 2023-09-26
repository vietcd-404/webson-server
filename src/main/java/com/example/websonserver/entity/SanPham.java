package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Table(name = "san_pham")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPham extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_san_pham")
    private Long maSanPham;

    @Column(name = "ten_san_pham")
    private String tenSanPham;

    @Column(name = "do_bong")
    private Integer doBong;

    @Column(name = "do_li")
    private Integer doLi;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "xoa")
    private Boolean xoa;

}
