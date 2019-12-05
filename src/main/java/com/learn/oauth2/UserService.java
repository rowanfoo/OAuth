package com.learn.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    PasswordEncoder passwordEncoder;

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("---------loadUserByUsername-------" + username);
        Authority a = new Authority("can_read");
//        return User.builder().authority(a).username("rowan").password( "{bcrypt}"+passwordEncoder.encode("rowan")).build();
        //       return User.builder().authority(a).username("rowan").password("{bcrypt}" + passwordEncoder.encode("rowan")).build();
        return User.builder().authority(a).username("rowan").password("rowan").build();


    }
}
