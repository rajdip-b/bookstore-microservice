package com.bookstorems.apigateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RouteFilter implements GlobalFilter {

    private final JWTTokenUtil jwtTokenUtil;

    public RouteFilter(JWTTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Any path has the convention: /<service>/<category>/**
        // Service is the lower case service name
        // Category is 'global', 'admin' or 'user'
        // We want to see if the path is /admin or /user then we do the authentication
        var path = exchange.getRequest().getPath().toString().split("/")[1];
        if (path.equals("admin") || path.equals("user")) {
            // Check if the 'Authorization' header is present
            if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
                return sendError(exchange, "Authorization header missing", HttpStatus.BAD_REQUEST);
            }

            // Fetch the token from the header
            String token = exchange.getRequest().getHeaders().getOrEmpty("Authorization").get(0).substring(7);

            try {
                // Check if the token is valid
                var userDTO = jwtTokenUtil.authorizeToken(token);

                // If valid, set the user id, email and role in the header
                exchange.getRequest().mutate()
                        .header("userId", userDTO.id().toString())
                        .header("email", userDTO.email())
                        .header("role", userDTO.role())
                        .build();
            } catch (Exception e) {
                // If invalid, return error
                return sendError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> sendError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        var response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Error", err);
        return response.setComplete();
    }

}
