package com.ra.service;

import com.ra.dto.request.SignInRequest;
import com.ra.dto.response.TokenResponse;
import com.ra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public TokenResponse authenticate(SignInRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username or password is incorrect"));

        String accessToken = jwtService.generateToken(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken("refresh_token")
                .userId(user.getId())
                .build();
    }
}
