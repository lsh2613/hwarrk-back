package com.hwarrk.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisTokenUtil {
    private final RedisTemplate<String, Long> refreshTokenTemplate;
    private final RedisTemplate<String, Long> blackListTokenTemplate;

    public void setRefreshToken(String key, Long value) {
        ValueOperations<String, Long> valueOperations = refreshTokenTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setRefreshTokenExpire(String key, Long value, Duration duration) {
        ValueOperations<String, Long> valueOperations = refreshTokenTemplate.opsForValue();
        valueOperations.set(key, value, duration);
    }

    public Long getMemberId(String key) {
        ValueOperations<String, Long> valueOperations = refreshTokenTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void deleteRefreshToken(String key) {
        refreshTokenTemplate.delete(key);
    }

    public void setBlackListTokenExpire(String key, Long value, Long milliSeconds) {
        ValueOperations<String, Long> blackListVP = blackListTokenTemplate.opsForValue();
        blackListVP.set(key, value, milliSeconds, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklistedToken(String token) {
        return blackListTokenTemplate.hasKey(token);
    }
}
