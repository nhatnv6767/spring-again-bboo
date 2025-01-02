package com.ra.service.impl;

import com.ra.service.JwtService;
import com.ra.util.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.token.secretKey}")
    private String secretKey;

    @Value("${jwt.token.refreshKey}")
    private String refreshKey;

    @Value("${jwt.token.expirationMs}")
    private String expirationMs;

    @Value("${jwt.token.refreshExpirationMs}")
    private String refreshExpirationMs;

    @Override
    public String generateToken(UserDetails user) {
        // TODO: Implement JWT token generation
        return generateToken(new HashMap<>(), user);
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        return generateRefreshToken(new HashMap<>(), user);
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaims(token, type, Claims::getSubject);
    }

    @Override
    public boolean isValid(String token, TokenType type, UserDetails userDetails) {
        final String username = extractUsername(token, type);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token, type);
    }

    private boolean isTokenExpired(String token, TokenType type) {
        return extractClaims(token, type, Claims::getExpiration).before(new Date());
    }

    private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expirationMs))) // 5 minutes
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(refreshExpirationMs))) // 100 days
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        byte[] keyBytes;
        if (type.equals(TokenType.ACCESS_TOKEN)) {
            keyBytes = Decoders.BASE64URL.decode(secretKey);
        } else {
            keyBytes = Decoders.BASE64URL.decode(refreshKey);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaims(String token, TokenType type, Function<Claims, T> claimResolver) {
        final Claims claims = extraAllClaims(token, type);
        return claimResolver.apply(claims);
    }

    private Claims extraAllClaims(String token, TokenType type) {
        return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
    }
}
