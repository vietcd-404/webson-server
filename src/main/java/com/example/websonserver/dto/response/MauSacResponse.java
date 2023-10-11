package com.example.websonserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MauSacResponse {
    private Long maMau;
    private String tenMau;
    private Integer trangThai;
    private Boolean xoa;
}
