package com.alert.open;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


@Service
@Slf4j
public class RedisService {
    private final RedissonClient redissonClient;
    private static final long DEFAULT_LEASE_TIME = 10000; // 10 seconds
    private static final long DEFAULT_WAIT_TIME = 5000; // 5 seconds

    @Autowired
    public RedisService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public <T> void setValue(String key, T value, Duration duration) {
        try {
            // Clear any existing key of wrong type
            RType keyType = redissonClient.getKeys().getType(key);
            if (keyType != null && keyType != RType.OBJECT) {
                redissonClient.getKeys().delete(key);
            }

            RBucket<T> bucket = redissonClient.getBucket(key);
            bucket.set(value, duration);
            log.debug("Successfully set value for key: {}", key);
        } catch (Exception e) {
            log.error("Failed to set value for key: {}", key, e);
            throw new RuntimeException("Redis cache operation failed", e);
        }
    }

    public <T> T getValue(String key, Class<T> type) {
        try {
            // First, check if key exists and get its type
            RType keyType = redissonClient.getKeys().getType(key);
            if (keyType != null && keyType != RType.OBJECT) {
                log.warn("Key {} exists with wrong type: {}. Removing key.", key, keyType);
                redissonClient.getKeys().delete(key);
            }

            RBucket<T> bucket = redissonClient.getBucket(key);
            T value = bucket.get();
            log.debug("Successfully retrieved value for key: {}", key);
            return value;
        } catch (Exception e) {
            log.error("Failed to get value for key: {}", key, e);
            throw new RuntimeException("Redis cache retrieval failed", e);
        }
    }

    public RLock getLock(String lockKey) {
        return redissonClient.getLock(lockKey);
    }

    public boolean tryLock(RLock lock, long waitTime, long leaseTime) {
        try {
            boolean acquired = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            if (acquired) {
                log.debug("Successfully acquired lock: {}", lock.getName());
            } else {
                log.warn("Failed to acquire lock after waiting: {}", lock.getName());
            }
            return acquired;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Lock acquisition interrupted: {}", lock.getName(), e);
            throw new RuntimeException("Lock acquisition interrupted", e);
        } catch (Exception e) {
            log.error("Error acquiring lock: {}", lock.getName(), e);
            throw new RuntimeException("Lock acquisition failed", e);
        }
    }

    public <T> T executeWithLock(String lockKey, long waitTime, long leaseTime, Supplier<T> task) {
        RLock lock = getLock(lockKey);
        boolean lockAcquired = false;

        try {
            lockAcquired = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            if (!lockAcquired) {
                throw new RuntimeException("Failed to acquire lock: " + lockKey);
            }

            return task.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Lock acquisition interrupted", e);
        } catch (Exception e) {
            throw new RuntimeException("Operation failed", e);
        } finally {
            if (lockAcquired && lock.isHeldByCurrentThread()) {
                try {
                    lock.unlock();
                    log.debug("Lock released: {}", lockKey);
                } catch (IllegalMonitorStateException e) {
                    log.warn("Failed to release lock - possible expiration: {}", lockKey, e);
                }
            }
        }
    }
}