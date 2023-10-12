package com.example.websonserver.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Table(name = "thuong_hieu")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThuongHieu extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_thuong_hieu")
    private Long maThuongHieu;

    @Column(name = "ten_thuong_hieu")
    private String tenThuongHieu;

    @Column(name = "trang_thai")
    private int trangThai;

    @Column(name = "xoa")
    private Boolean xoa;

}
