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


//    public NguoiDung map(NguoiDung nd) throws ParseException {
//        nd.setEmail(this.getEmail());
//        nd.setGioiTinh(Integer.parseInt(this.getGioiTinh()));
//        nd.setAnh(this.getAnh());
//        nd.setNgaySinh(this.getNgaySinh());
//        nd.setUsername((this.getUsername()));
////        nd.setPassword(this.getPassword());
//        nd.setSdt(this.getSdt());
//        nd.setHo(this.getHo());
//        nd.setTenDem(this.getTenDem());
//        nd.setTen(this.getTen());
//        nd.setXoa(this.getXoa());
//        nd.setVaiTro(VaiTro.builder().maVaiTro(Long.valueOf(this.getVaiTro())).build());
//        nd.setTrangThai(this.getTrangThai());
//        return nd;
//    }
}