package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "danh_sach_yeu_thich")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DanhSachYeuThich extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_danh_sach")
    private Long maDanhSach;

    @ManyToOne
    @JoinColumn(name = "ma_san_pham_chi_tiet")
    private SanPhamChiTiet sanPhamChiTiet ;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung nguoiDung;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "xoa")
    private Boolean xoa;
}
