package com.dharma.oauth2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Oauth2ApplicationTests {
    @Autowired
    PasswordEncoder mouserPasswordEncoder;
    @Test
    public void contextLoads() {
        System.out.println("-------" + mouserPasswordEncoder.encode("rowan"));
    }

}
