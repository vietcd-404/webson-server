package com.example.websonserver.dto.request;

import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.entity.VaiTro;
import com.example.websonserver.entity.VaiTroNguoiDung;
import com.example.websonserver.service.serviceIpml.VaiTroServiceImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Data
public class NguoiDungRequest {

    @NotBlank(message = "Không bỏ trống email")
    @NotNull
    private String email;

    @NotBlank(message = "Không bỏ trống giới tính")
    @NotNull
    private String gioiTinh;

    //    @NotBlank(message = "Không bỏ trống ảnh")
//    @NotNull
    private String anh;

    @NotNull(message = "Không bỏ trống ngày sinh")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date ngaySinh;

    @NotBlank(message = "Không bỏ trống username")
    @NotNull
    private String username;

    @NotBlank(message = "Không bỏ trống password")
    @NotNull
    private String password;

    @NotBlank(message = "Không bỏ trống số điện thoại")
    @NotNull
    @Pattern(regexp = "^(\\+84|0)[3|5|7|8|9][0-9]{8}$", message = "Số điện thoại không hợp lệ")
    private String sdt;

    @NotBlank(message = "Không bỏ trống họ")
    @NotNull
    private String ho;

    @NotBlank(message = "Không bỏ trống tên đệm")
    @NotNull
    private String tenDem;

    @NotBlank(message = "Không bỏ trống tên")
    @NotNull
    private String ten;

    private Boolean xoa = false;

    private String vaiTro;

    private Integer trangThai = 0;

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