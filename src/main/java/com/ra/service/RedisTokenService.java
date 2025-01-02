package com.ra.service;

import com.ra.model.RedisToken;
import com.ra.repository.RedisTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTokenService {
    private final RedisTokenRepository redisTokenRepository;

    public void save(RedisToken token) {
        redisTokenRepository.save(token);
    }
}
