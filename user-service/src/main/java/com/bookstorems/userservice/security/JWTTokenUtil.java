package com.bookstorems.userservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bookstorems.userservice.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTTokenUtil {

    @Value("${jwt.validity}")
    private Long TOKEN_VALIDITY;
    @Value("${jwt.secret}")
    private String SECRET;

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8));
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .withIssuer("BookstoreMS")
                .withClaim("issuedAt", new Date())
                .withClaim("role", retrieveRole(user.getEmail()))
                .withClaim("id", user.getId())
                .sign(algorithm);
    }

    private String retrieveRole(String email) throws JWTVerificationException {
        if (email.equals("admin@bookstore.com"))
            return "ROLE_ADMIN";
        return "ROLE_USER";
    }

}