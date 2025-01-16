package com.example.trananhthi.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    String generateToken(String email);

    Date extractExpiration(String token);

    String extractUsername(String token);

    Boolean validateToken(String token, UserDetails userDetails);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
}
