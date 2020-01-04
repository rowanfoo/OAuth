package com.dharma.oauth2.service;

import com.dhamma.pesistence.entity.data.QUser;
import com.dhamma.pesistence.entity.data.User;
import com.dhamma.pesistence.entity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepo userRepo;

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("---------loadUserByUsername-------" + username);
        User user = userRepo.findOne(QUser.user.username.eq(username)).get();
        return user;

    }
}
