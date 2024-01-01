package com.example.websonserver.dto.response;


import java.util.Date;

public class JwtResponse {
    private String token;
    private String type ="Bearer";
    private Long id;
    private String username;
    private String password;
    private String email;
    private String sdt;
    private String vaiTro;
    private String ho;
    private String tenDem;
    private String ten;
    private Date ngaySinh;
    private Integer gioiTinh;
    private Integer trangThai;




//    public JwtResponse(String token, String username, String email, String sdt, String vaiTro) {
//        this.token = token;
//        this.username = username;
//        this.email = email;
//        this.sdt = sdt;
//        this.vaiTro = vaiTro;
//
//    }


    public JwtResponse(String token, Long id,String username, String password,
                       String email, String sdt, String vaiTro, String ho, String tenDem,
                       String ten, Date ngaySinh, Integer gioiTinh,Integer trangThai) {
        this.id = id;
        this.token = token;
        this.type = type;
        this.username = username;
        this.password = password;
        this.email = email;
        this.sdt = sdt;
        this.vaiTro = vaiTro;
        this.ho = ho;
        this.tenDem = tenDem;
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.trangThai = trangThai;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTenDem() {
        return tenDem;
    }

    public void setTenDem(String tenDem) {
        this.tenDem = tenDem;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public Integer getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(Integer gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
