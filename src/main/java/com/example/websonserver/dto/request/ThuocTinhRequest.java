package com.example.websonserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThuocTinhRequest {
    private Long maLoai;
    private Long maSanPham;
    private Long maMau;
    private Long maThuongHieu;
    private String tenSanPham;
    private BigDecimal giaThap;
    private BigDecimal giaCao;
    private Boolean giaTangDan;
    private Boolean giaGiamDan;
    private Integer trangThai;

}
