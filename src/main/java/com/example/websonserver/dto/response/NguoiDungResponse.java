package com.example.websonserver.dto.response;


import com.example.websonserver.entity.VaiTroNguoiDung;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDungResponse {
    private Long maNguoiDung;
    private Date ngaySinh;
    private String email;
    private Integer gioiTinh;
    private String username;
    private String password;
    private String sdt;
    private String ho;
    private String tenDem;
    private String ten;
    private Integer trangThai;
    private String vaiTro;
    private Integer luotMua;
}
