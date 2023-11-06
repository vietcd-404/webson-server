package com.example.websonserver.dto.response;



public class JwtResponse {
    private String token;
    private String type ="Bearer";
    private String username;
    private String email;
    private String sdt;
    private String vaiTro;



    public JwtResponse(String token, String username, String email, String sdt, String vaiTro) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.sdt = sdt;
        this.vaiTro = vaiTro;

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
}
