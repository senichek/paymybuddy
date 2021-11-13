package com.open.paymybuddy.utils;

import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.security.UserPrincipal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    // TODO удалить
    public static final Integer LOGGED_IN_USER = 1;
    public static final String LOGGED_IN_USER_EMAIL = "james@gmail.com";

    public static Person getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getPerson();
    }
}
