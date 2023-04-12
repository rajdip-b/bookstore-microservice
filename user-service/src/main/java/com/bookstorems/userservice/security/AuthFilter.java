package com.bookstorems.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This class looks for incoming requests and checks if the request is trying
 * to access an authorized resource. If it is, then we need to authorize the
 * request. The authentication is already done by the api-gateway. ALl we need
 * to do is to retrieve the userId and role from the header and set it in the
 * SecurityContext.
 */
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get the headers from the request
        var email = request.getHeader("email");
        var role = request.getHeader("role");
        var userId = request.getHeader("userId");

        // If the headers are not present, then the user is not authenticated
        if (email == null || role == null || userId == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // If the headers are present, then the user is authenticated
        var token = new CustomAuthenticationToken(email, role, userId);
        SecurityContextHolder.getContext().setAuthentication(token);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        var path = request.getServletPath();
        return !(path.startsWith("/user") || path.startsWith("/admin"));
    }
}
