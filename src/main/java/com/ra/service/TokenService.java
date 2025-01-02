package com.ra.service;

import com.ra.model.Token;
import com.ra.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record TokenService(TokenRepository tokenRepository) {
    public int save(Token token) {
        Optional<Token> optional = tokenRepository.findUsername(token.getUsername());
        if (optional.isEmpty()) {
            tokenRepository.save(token);
        } else {
            Token currentToken = optional.get();
            currentToken.setAccessToken(token.getAccessToken());
            currentToken.setRefreshToken(token.getRefreshToken());
            tokenRepository.save(currentToken);
        }
        return token.getId();
    }

    public String delete(Token token) {
        return "Token deleted";
    }
}
