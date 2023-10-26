package com.example.websonserver.config.sercurity;



import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.entity.VaiTroNguoiDung;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetail implements UserDetails {

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private String sdt;

    private int trangThai;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
  //map từ thông tin user chuyển sang thông tin custumUserDetail

    public static CustomUserDetail mapUserDetail(NguoiDung nguoiDung){
        VaiTroNguoiDung authorityName = nguoiDung.getVaiTro().getTenVaiTro();
        GrantedAuthority authority = new SimpleGrantedAuthority(authorityName.name());
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);
        return new CustomUserDetail(
                nguoiDung.getMaNguoiDung(),
                nguoiDung.getUsername(),
                nguoiDung.getPassword(),
                nguoiDung.getSdt(),
                nguoiDung.getEmail(),
                nguoiDung.getTrangThai(),
                authorities
        );
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
