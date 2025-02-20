package com.alert.open;

import java.time.Duration;

public interface RedisCacheService<T>{
    T get(String key, Class<T> clazz);
    void set(String key, Object value);
    void set(String key, Object value, Duration duration);
}
