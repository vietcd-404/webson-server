package com.example.websonserver.config.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService implements EmailSender{



    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText("Mã OTP của bạn là: " + otp);
            helper.setTo(to);
            helper.setSubject("Mã OTP của bạn");
            helper.setFrom("hello@amigoscode.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }


    @Override
    @Async
    public void sendKhachdatHang(String to, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText("Thư cảm ơn: \n" + otp);
            helper.setTo(to);
            helper.setSubject("Cảm ơn bạn đã đặt hàng tại HEVA Shop");
            helper.setFrom("hello@amigoscode.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }

    @Override
    public void sendThongBao(String to, String text,String thongBao) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(text);
            helper.setTo(to);
            helper.setSubject("HEVA Shop thông báo đơn hàng của bạn " +thongBao);
            helper.setFrom("hello@amigoscode.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }
}
