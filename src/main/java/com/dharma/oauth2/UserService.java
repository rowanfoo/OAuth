package com.dharma.oauth2;

import com.dhamma.pesistence.entity.data.QUser;
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
        Authority a = new Authority("can_read");
        userRepo.findOne(QUser.user.userid.eq(username));

        com.dhamma.pesistence.entity.data.User user = userRepo.findOne(QUser.user.userid.eq(username)).orElseThrow(
                () -> new UsernameNotFoundException("User not found: " + username)
        );
        return User.builder().authority(a).username("rowan").password("rowan").build();
    }
}
