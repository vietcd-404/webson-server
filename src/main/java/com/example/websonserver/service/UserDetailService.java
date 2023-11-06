package com.example.websonserver.service;



import com.example.websonserver.config.sercurity.CustomUserDetail;
import com.example.websonserver.entity.NguoiDung;
import com.example.websonserver.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private NguoiDungRepository usersRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       NguoiDung users= usersRepo.findByUsername(username);
       if (users==null){
           throw new UsernameNotFoundException("User not found");
       }
//        if (users.getTrangThai()==0) {
//            throw new DisabledException("Tài khoản chưa kích hoạt");
//        }
       return CustomUserDetail.mapUserDetail(users);
    }
}
