package com.example.websonserver.api;


import com.example.websonserver.config.email.EmailService;
import com.example.websonserver.config.sercurity.CustomUserDetail;
import com.example.websonserver.dto.request.LoginRequest;
import com.example.websonserver.dto.request.NguoiDungRequest;
import com.example.websonserver.dto.request.SignupRequest;
import com.example.websonserver.dto.response.JwtResponse;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.entity.VaiTro;
import com.example.websonserver.entity.VaiTroNguoiDung;
import com.example.websonserver.jwt.JwtTokenProvider;
import com.example.websonserver.service.serviceIpml.NguoiDungServiceImpl;
import com.example.websonserver.service.serviceIpml.VaiTroServiceImpl;
import com.example.websonserver.util.OtpUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth/")
public class AuthApi {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private NguoiDungServiceImpl nguoiDungService;

    @Autowired
    private VaiTroServiceImpl vaiTroService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private EmailService emailService;

    private static final int OTP_TTL_MINUTES = 2;

    private Map<String, String> otpMap = new HashMap<>();

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
//            if (customUserDetail.getTrangThai() == 0) {
//                // Tài khoản chưa kích hoạt, trả về phản hồi tài khoản chưa kích hoạt
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Tài khoản chưa kích hoạt"));
//            }
            //Sinh ra JWT trả vê client
            String token = jwtTokenProvider.genToken(customUserDetail);

            //Lấy các quyền của user
            String role = customUserDetail.getAuthorities().iterator().next().getAuthority();

            return ResponseEntity.ok(new JwtResponse(token,customUserDetail.getId(),customUserDetail.getUsername(), customUserDetail.getPassword(),customUserDetail.getSdt(),
                    customUserDetail.getEmail(), role, customUserDetail.getHo(),
                    customUserDetail.getTenDem(), customUserDetail.getTen(),
                    customUserDetail.getNgaySinh(), customUserDetail.getGioiTinh(),customUserDetail.getTrangThai()));
        } catch (AuthenticationException e) {
            // Xử lý trường hợp sai tài khoản hoặc mật khẩu
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Sai tài khoản hoặc mật khẩu"));
        }

    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest signupRequest, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if (nguoiDungService.existByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username đã tồn tại"));
        }
        if (nguoiDungService.existByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email đã tồn tại"));
        }
        NguoiDung users = new NguoiDung();
        users.setUsername(signupRequest.getUsername());
        users.setEmail(signupRequest.getEmail());
        users.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        users.setTrangThai(signupRequest.getTrangThai());
        users.setXoa(signupRequest.getXoa());
        users.setSdt(signupRequest.getSdt());
        LocalDateTime otpExpirationTime = LocalDateTime.now().plus(OTP_TTL_MINUTES, ChronoUnit.MINUTES);
        String otp = otpUtil.generateOtp();
        otpMap.put(signupRequest.getEmail(), otp);
        emailService.send(signupRequest.getEmail(), otp);
        String str = signupRequest.getVaiTro();
        VaiTro vaiTro = null;
        if (str == null) {
            vaiTro = vaiTroService.findByRoleName(VaiTroNguoiDung.ROLE_USER).orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
        } else {
            if (signupRequest.getVaiTro().equals("admin")) {
                vaiTro = vaiTroService.findByRoleName(VaiTroNguoiDung.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
            } else if (signupRequest.getVaiTro().equals("user")) {
                vaiTro = vaiTroService.findByRoleName(VaiTroNguoiDung.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
            } else if (signupRequest.getVaiTro().equals("staff")) {
                vaiTro = vaiTroService.findByRoleName(VaiTroNguoiDung.ROLE_STAFF)
                        .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
            }
        }
        users.setVaiTro(vaiTro);
        users.setGioiTinh(1);
        nguoiDungService.saveOrUpdate(users);
        return ResponseEntity.ok(new MessageResponse("Đăng kí thành công"));
    }

    @PostMapping("/active")
    public ResponseEntity<?> active(@RequestParam("email") String email, @RequestParam("otp") String otp) {
        String storedOtp = otpMap.get(email);
        System.out.println("Stored OTP: " + storedOtp);

        NguoiDung nguoiDung = nguoiDungService.findByEmail(email);
        if (nguoiDung == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email không tồn tại"));
        }
        if (nguoiDung.getTrangThai() == 1) {
            return ResponseEntity.badRequest().body(new MessageResponse("Đã kích hoạt"));
        }
        if (storedOtp == null || !storedOtp.equals(otp)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Sai OTP"));
        }
        nguoiDung.setTrangThai(1);
        nguoiDungService.saveOrUpdate(nguoiDung);
        otpMap.remove(email);
        return ResponseEntity.ok(new MessageResponse("Kích hoạt tài khoản thành công"));
    }

    @GetMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam("email") String email) {
        LocalDateTime otpExpirationTime = LocalDateTime.now().plus(OTP_TTL_MINUTES, ChronoUnit.MINUTES);
        if (otpMap.containsKey(email)) {
            LocalDateTime currentTime = LocalDateTime.now();
            if (currentTime.isBefore(otpExpirationTime)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Mã OTP đã được gửi đi và vẫn còn hiệu lực."));
            } else if (currentTime.isAfter(otpExpirationTime)) {
                return ResponseEntity.badRequest().body(new MessageResponse(
                        " OTP đã hết hạn. Vui lòng yêu cầu OTP mới."));
            }
        }

        NguoiDung nguoiDung = nguoiDungService.findByEmail(email);
        if (nguoiDung == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email không tồn tại"));
        }
        if (nguoiDung.getTrangThai() == 1) {
            return ResponseEntity.badRequest().body(new MessageResponse("Đã kích hoạt"));
        }
        String newOtp = otpUtil.generateOtp();
        otpMap.put(email, newOtp);
        emailService.send(email, newOtp);
        return ResponseEntity.ok(new MessageResponse("Đã gửi lại thành công"));
    }

    @GetMapping("/ok")
    public ResponseEntity<?> ok() {
        return ResponseEntity.ok("Ok nha");
    }
}
