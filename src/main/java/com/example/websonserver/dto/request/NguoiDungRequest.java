package com.example.websonserver.dto.request;

import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.entity.PhuongThucThanhToan;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class NguoiDungRequest {

    private Long maNguoiDung;

    @Email(message = "Sai định dạng email")
    @NotBlank(message = "Không bỏ trống")
    private String email;

    private Integer gioiTinh;

    private String anh;

    private Date ngaySinh;

    @NotBlank(message = "Không bỏ trống")
    private String username;

    @NotBlank(message = "Không bỏ trống")
    private String password;

    @NotBlank(message = "Không bỏ trống")
    private String ho;

    @NotBlank(message = "Không bỏ trống")
    private String tenDem;

    @NotBlank(message = "Không bỏ trống")
    private String ten;


    private Integer trangThai = 0;

    private Boolean xoa = true;

    public NguoiDung map(NguoiDung nguoiDung){
        nguoiDung.setTen(this.getTen());
        nguoiDung.setHo(this.getHo());
        nguoiDung.setTenDem(this.getTenDem());
        nguoiDung.setPassword(this.getPassword());
        nguoiDung.setUsername(this.getUsername());
        nguoiDung.setNgaySinh(this.getNgaySinh());
        nguoiDung.setGioiTinh(this.getGioiTinh());
        nguoiDung.setAnh(this.getAnh());
        nguoiDung.setTrangThai(this.getTrangThai());
        nguoiDung.setXoa(this.getXoa());
        return nguoiDung;
    }
}
