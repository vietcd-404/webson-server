package com.example.websonserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThuongHieuReponse {
    private Long maThuongHieu;
    private String tenThuongHieu;
    private Boolean xoa;
    private Integer trangThai;
}
