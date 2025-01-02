package com.ra.service;

import com.ra.dto.request.SignInRequest;
import com.ra.dto.response.SignInResponse;
import com.ra.model.Token;
import com.ra.model.User;
import com.ra.repository.TokenRepository;
import com.ra.repository.UserRepository;
import com.ra.util.TokenType;
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
    private final TokenService tokenService;

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

                // save token to db
                tokenService.save(Token.builder()
                        .username(user.getUsername())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build());


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
        String userName = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        // check it into database
        Optional<User> user = userRepository.findByUsername(userName);
        if (!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN, user.get())) { // get() because User is optional
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

    public String logout(HttpServletRequest request) {
        String accessToken = request.getHeader("x-token");
        if (!StringUtils.hasText(accessToken)) {
            throw new BadCredentialsException("Invalid token");
        }

        // extract username from token
        String userName = jwtService.extractUsername(accessToken, TokenType.ACCESS_TOKEN);
        Token currentToken = tokenService.getByUsername(userName);

        tokenService.delete(currentToken);

        return "Logout successful";

    }
}
