package com.example.websonserver.jwt;


import com.example.websonserver.config.sercurity.CustomUserDetail;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${ra.jwt.secret}")
    private String JWT_SECRET;

    @Value("${ra.jwt.expiration}")
    private int JWT_EXPIRATION;

    public String genToken(CustomUserDetail customUserDetail){
        Date date = new Date();
        Date expiryDate = new Date(date.getTime()+JWT_EXPIRATION);
        //Tạo ra chuỗi token từ UserName
        return Jwts.builder().setSubject(customUserDetail.getUsername())
                .setIssuedAt(date)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,JWT_SECRET).compact();

    }

    //Lấy thông tin từ JWT
    public String getUserNameFromJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET)
                .parseClaimsJws(token).getBody();
        //trả lại thông tin username
        return claims.getSubject();
    }

    public boolean validationToken(String authToken){
        try {
            Jwts.parser().setSigningKey(JWT_SECRET)
                    .parseClaimsJws(authToken);
            return true;
        }catch (MalformedJwtException ex){
            log.error("Invalid JWT Token");
        }catch (ExpiredJwtException ex){
            log.error("Expired JWT Token");
        }catch (UnsupportedJwtException ex){
            log.error("Unsupported JWT Token");
        }catch (IllegalArgumentException ex){
            log.error("JWT claims String is empty");
        }
        return false;
    }

}
