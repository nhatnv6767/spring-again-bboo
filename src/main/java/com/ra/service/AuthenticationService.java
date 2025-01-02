package com.ra.service;

import com.ra.dto.request.SignInRequest;
import com.ra.dto.response.SignInResponse;
import com.ra.model.User;
import com.ra.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public SignInResponse signIn(SignInRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            if (authentication.isAuthenticated()) {
                var user = userRepository.findByUsername(request.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                String accessToken = jwtService.generateToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                return SignInResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .userId(user.getId())
                        .phoneNumber(user.getPhone())
                        .role(user.getType().name())
                        .build();
            } else {
                throw new BadCredentialsException("Invalid username or password");
            }
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {}", request.getUsername(), e);
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public SignInResponse refresh(HttpServletRequest request) {
//        System.out.println(request.getHeader("x-token"));
        String refreshToken = request.getHeader("x-token");
        if (!StringUtils.hasLength(refreshToken)) {
            throw new BadCredentialsException("Invalid token");
        }
        // extract username from token
        String userName = jwtService.extractUsername(refreshToken);
        // check it into database
        Optional<User> user = userRepository.findByUsername(userName);
        if (!jwtService.isValid(refreshToken, user.get())) { // get() because User is optional
            throw new BadCredentialsException("Invalid token");
        }
        String accessToken = jwtService.generateToken(user.get());

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.get().getId())
                .phoneNumber(user.get().getPhone())
                .role(user.get().getType().name())
                .build();
    }
}
