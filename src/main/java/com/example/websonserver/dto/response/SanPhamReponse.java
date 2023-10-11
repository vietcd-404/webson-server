package com.example.websonserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamReponse {
    private Long maSanPham;
    private String tenSanPham;
    private Integer doBong;
    private Integer doLi;
    private Boolean xoa;
    private Integer trangThai;
}
