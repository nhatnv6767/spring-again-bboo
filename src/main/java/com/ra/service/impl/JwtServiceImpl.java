package com.ra.service.impl;

import com.ra.service.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {
    @Override
    public String generateToken(UserDetails user) {
        // TODO: Implement JWT token generation
        return "access-token";
    }
}
