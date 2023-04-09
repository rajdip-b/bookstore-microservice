package com.bookstorems.cartservice.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getUserId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
