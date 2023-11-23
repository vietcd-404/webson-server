package com.example.websonserver.config.sercurity;



import com.example.websonserver.entity.VaiTroNguoiDung;
import com.example.websonserver.jwt.JwtAuthFilter;
import com.example.websonserver.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        jsr250Enabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    @Qualifier("customAuthenticationEntryPoint")
    private AuthenticationEntryPoint authEntryPoint;

    @Autowired
    @Qualifier("customAccessDeniedHandler")
    private CustomAccessDeniedHandler accessDenied;

    @Bean
    public JwtAuthFilter jwtAuthFilter(){
        return new JwtAuthFilter();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeHttpRequests()
                .requestMatchers("/api/admin/**").hasAnyAuthority(VaiTroNguoiDung.ROLE_ADMIN.name())
                .requestMatchers("/api/user/**").hasAnyAuthority(VaiTroNguoiDung.ROLE_USER.name(),VaiTroNguoiDung.ROLE_ADMIN.name())
                .requestMatchers("/api/staff/**").hasAnyAuthority(VaiTroNguoiDung.ROLE_STAFF.name(),VaiTroNguoiDung.ROLE_ADMIN.name())
                .requestMatchers("/api/auth/**").permitAll() //cho phép vào không cần đăng nhập
                .requestMatchers("/api/account/**").permitAll()
                .requestMatchers("/api/vnpay/**").permitAll()
                .requestMatchers("/api/anh/**").permitAll()
                .requestMatchers("/api/guest/**").permitAll()
//                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http
//                .exceptionHandling()
//                .authenticationEntryPoint(authEntryPoint)
//                .accessDeniedHandler(accessDenied);
        http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
