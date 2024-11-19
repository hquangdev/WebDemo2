package com.example.furniturestore.Service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class RedisLockService {
    private final StringRedisTemplate redisTemplate;

    // Thời gian sống của khóa
    private static final long LOCK_TIMEOUT = 10;

    public RedisLockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Phương thức để cố gắng lấy lock
    public boolean acquireLock(String lockKey) {
        // Thử đặt khóa với giá trị "locked"
        Boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", LOCK_TIMEOUT, TimeUnit.SECONDS);
        return isSuccess != null && isSuccess;
    }

    // Phương thức giải phóng lock
    public void releaseLock(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}
