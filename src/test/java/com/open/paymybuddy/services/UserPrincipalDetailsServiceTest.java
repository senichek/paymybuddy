package com.open.paymybuddy.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.open.paymybuddy.security.UserPrincipalDetailsService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
public class UserPrincipalDetailsServiceTest {

    @Autowired
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @Test
    public void loadUserByUsernameExceptionTest() throws Exception {

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> userPrincipalDetailsService.loadUserByUsername("email"));
        assertTrue(exception.getMessage().contains("User not found"));
    }
}
