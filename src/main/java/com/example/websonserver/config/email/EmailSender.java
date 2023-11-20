package com.example.websonserver.config.email;

public interface EmailSender {
    void send(String to, String email);

    void sendKhachdatHang(String to, String thongBao);

    void sendThongBao(String to, String thongBao,String text);
}
