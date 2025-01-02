package com.ra.service;

import com.ra.model.RedisToken;
import com.ra.repository.RedisTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTokenService {
    private final RedisTokenRepository redisTokenRepository;

    public String save(RedisToken token) {
        RedisToken result = redisTokenRepository.save(token);
        return result.getId();
    }

    public RedisToken findById(String id) {
        return redisTokenRepository.findById(id).orElse(null);
    }

    public void deleteById(String id) {
        redisTokenRepository.deleteById(id);
    }
}
