package com.ra.controller;

import com.ra.dto.request.ResetPasswordDTO;
import com.ra.dto.request.SignInRequest;
import com.ra.dto.response.SignInResponse;
import com.ra.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication Controller")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/access-token")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<SignInResponse> refresh(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.refresh(request), HttpStatus.OK);
    }

    @PostMapping("/remove-token")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.removeToken(request), HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        return new ResponseEntity<>(authenticationService.forgotPassword(email), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody String secretKey) {
        return new ResponseEntity<>(authenticationService.resetPassword(secretKey), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ResetPasswordDTO request) {
        return new ResponseEntity<>(authenticationService.changePassword(request), HttpStatus.OK);
    }


}
