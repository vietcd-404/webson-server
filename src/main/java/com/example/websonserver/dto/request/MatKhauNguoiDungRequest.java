package com.example.websonserver.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class MatKhauNguoiDungRequest {

    @NotBlank(message = "Không bỏ trống password")
    @NotNull
    private String oldpassword;

    @NotBlank(message = "Không bỏ trống số điện thoại")
    @NotNull
    private String newpass;

    @NotBlank(message = "Không bỏ trống họ")
    @NotNull
    private String repass;

}