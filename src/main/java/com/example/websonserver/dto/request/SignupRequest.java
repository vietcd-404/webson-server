package com.example.websonserver.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank(message = "Không bỏ trống username")
    private String username;

    @NotBlank(message = "Không bỏ trống password")
    private String password;

    @Email(message = "Email sai định dạng")
    private String email;

    @NotBlank(message = "Không bỏ trống số điện thoại")
    private String sdt;

    private int trangThai = 0;
    private  Boolean xoa = false;
    private String vaiTro;
}
