package com.example.websonserver.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Table(name = "anh_san_pham_chi_tiet")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnhSanPham extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_anh")
    private Long maAnh;

    @Lob // Large Object
    @Column(name = "anh")
    private String anh;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "xoa")
    private Boolean xoa;

    @ManyToOne
    @JoinColumn(name = "ma_san_pham_chi_tiet")
    private SanPhamChiTiet sanPhamChiTiet;
}
