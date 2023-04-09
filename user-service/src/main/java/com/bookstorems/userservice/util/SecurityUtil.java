package com.bookstorems.userservice.util;

import com.bookstorems.userservice.security.CustomAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getUserId() {
        return Long.parseLong(getAuthentication().getUserId());
    }

    public static String getEmail() {
        return getAuthentication().getEmail();
    }

    private static CustomAuthenticationToken getAuthentication() {
        return (CustomAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

}
