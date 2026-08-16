package com.example.BookingHotel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    //lưu data voi custom TTL
    public void setToken(String token, String value, long ttl, TimeUnit unit){
        redisTemplate.opsForValue().set(token, value, ttl, unit);
    }

    //kiem tra neu token ton tai trong database
    public boolean hasToken(String token){
        return redisTemplate.hasKey(token);
    }
    //OTP
    public void setOtp(String email, String Opt, long ttl, TimeUnit unit){
        redisTemplate.opsForValue().set(email, Opt, ttl, unit);
    }

    public String getOtp(String keyOtp){
        return redisTemplate.opsForValue().get(keyOtp);
    }

    public void setVerifiedEmail(String email, String verified, int ttl, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(email, verified, ttl, timeUnit);
    }

    public boolean hasOtp(String verifiedOtp){
        return redisTemplate.hasKey(verifiedOtp);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }
}
