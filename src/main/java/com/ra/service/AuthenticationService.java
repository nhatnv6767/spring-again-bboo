package com.ra.service;

import com.ra.dto.request.SignInRequest;
import com.ra.dto.response.TokenResponse;
import com.ra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public TokenResponse authenticate(SignInRequest request) {

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username or password is incorrect"));

        return TokenResponse.builder()
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .userId(user.getId())
                .build();
    }
}
