package com.bookstorems.apigateway;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class JWTTokenUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    public UserDTO authorizeToken(String token) throws JWTVerificationException {
        var decodedToken = getDecodedToken(token);
        var email = retrieveEmailFromToken(decodedToken);
        var role = retrieveRoleFromToken(decodedToken);
        var id = retrieveUserIdFromToken(decodedToken);
        return new UserDTO(id, email, role);
    }

    private DecodedJWT getDecodedToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8));
        return JWT.require(algorithm).build().verify(token);
    }

    private String retrieveEmailFromToken(DecodedJWT decodedJWT) throws JWTVerificationException {
        return decodedJWT.getSubject();
    }

    private String retrieveRoleFromToken(DecodedJWT decodedJWT) throws JWTVerificationException {
        return decodedJWT.getClaim("role").asString();
    }

    private Long retrieveUserIdFromToken(DecodedJWT decodedJWT) throws JWTVerificationException {
        return decodedJWT.getClaim("id").asLong();
    }

}