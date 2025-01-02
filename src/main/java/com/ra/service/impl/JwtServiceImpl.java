package com.ra.service.impl;

import com.ra.service.JwtService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {
    @Override
    public String generateToken(UserDetails user) {
        // TODO: Implement JWT token generation
        return generateToken(new HashMap<>(), user);
    }

    private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1 day
                .compact();
    }
}
