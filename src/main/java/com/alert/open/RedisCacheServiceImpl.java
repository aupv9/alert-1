package com.alert.open;

import com.alert.open.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements RedisCacheService<Event> {

    private final RedisService redisService;


    @Override
    public Event get(String key, Class<Event> clazz) {
        return redisService.getValue(key, clazz);
    }

    @Override
    public void set(String key, Object value) {
        redisService.setValue(key, value, Duration.ofMinutes(1));
    }

    @Override
    public void set(String key, Object value, Duration duration) {
        redisService.setValue(key, value, duration);
    }
}
