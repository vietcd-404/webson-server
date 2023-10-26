package com.example.websonserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String username;
    private String password;
    private String email;
    private String sdt;
    private int trangThai = 0;
    private  Boolean xoa = false;
    private String vaiTro;
}
