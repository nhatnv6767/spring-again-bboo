package com.ra.service;

import com.ra.dto.request.ResetPasswordDTO;
import com.ra.dto.request.SignInRequest;
import com.ra.dto.response.SignInResponse;
import com.ra.model.RedisToken;
import com.ra.model.Token;
import com.ra.model.User;
import com.ra.repository.UserRepository;
import com.ra.util.TokenType;
import com.ra.util.UserStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static org.springframework.http.HttpHeaders.REFERER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RedisTokenService redisTokenService;

    public SignInResponse authenticate(SignInRequest request) {

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

                // save token to db postgres
//                tokenService.save(Token.builder()
//                        .username(user.getUsername())
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build());
                // save to redis
                redisTokenService.save(RedisToken.builder()
                        .id(user.getUsername())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .resetToken("nullfdsaf")
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
        String refreshToken = request.getHeader(REFERER);
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

        // save to redis for test
        redisTokenService.save(RedisToken.builder()
                .id(user.get().getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .resetToken("nullfdsaf")
                .build());

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.get().getId())
                .phoneNumber(user.get().getPhone())
                .role(user.get().getType().name())
                .build();
    }

    public String removeToken(HttpServletRequest request) {
        String accessToken = request.getHeader(REFERER);
        if (!StringUtils.hasText(accessToken)) {
            throw new BadCredentialsException("Invalid token");
        }

        // extract username from token
        String userName = jwtService.extractUsername(accessToken, TokenType.ACCESS_TOKEN);
        Token currentToken = tokenService.getByUsername(userName);

        // for postgres
//        tokenService.delete(currentToken);
        // for redis
        redisTokenService.deleteById(userName);

        return "Logout successful";

    }

    public String forgotPassword(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        // user is active or not
        if (!user.get().getStatus().equals(UserStatus.ACTIVE)) {
            throw new BadCredentialsException("User is not active");
        }

        // generate token-reset password
        String resetToken = jwtService.generateResetToken(user.get());

        //TODO: send email confirmLink
        String confirmLink = String.format("curl --location 'http://192.168.1.202:8080/auth/reset-password' \\\n" +
                "--header 'accept: */*' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--data '%s'", resetToken);
        log.info("Confirm link: {}", confirmLink);
        return "Email sent to " + email + " resetToken: " + resetToken;
    }

    public String resetPassword(String secretKey) {
        log.info("Reset password for secret key: {}", secretKey);
        // check secret key is valid or not
        isValidUserByToken(secretKey);

        // save to db
        // update password

        return "Password reset successfully";
    }

    public String changePassword(ResetPasswordDTO request) {
        User user = isValidUserByToken(request.getSecretKey());
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadCredentialsException("Password and confirm password not match");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.saveUser(user);

        return "Password changed successfully";
    }

    private User isValidUserByToken(String secretKey) {
        final String userName = jwtService.extractUsername(secretKey, TokenType.RESET_PASSWORD_TOKEN);
        var user = userRepository.findByUsername(userName);

        if (!user.get().getStatus().equals(UserStatus.ACTIVE)) {
            throw new BadCredentialsException("User is not active");
        }

        if (!jwtService.isValid(secretKey, TokenType.RESET_PASSWORD_TOKEN, user.get())) {
            throw new BadCredentialsException("Invalid token");
        }

        return user.get();
    }
}
