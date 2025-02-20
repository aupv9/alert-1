package com.alert.open;

import com.alert.open.entity.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceCommonImpl implements EventServiceCommon<Event, String> {
    private final RedisCacheService<Event> redisCacheService;

    private final EventServiceImpl eventService;

    private final CacheLocalService<Event> eventCacheLocalService;

    private final RedisService redisService;

    @Override
    public Event getEventById(String key) {
        Event event;
        final String PREFIX_KEY = "PRO_EVENT_KEY";
        final String cacheKey = PREFIX_KEY + key;
        RLock lock = null;

        try {
            event = redisCacheService.get(cacheKey, Event.class);
            if (event != null) {
                log.info("Retrieved from Redis cache: {}", event);
                eventCacheLocalService.put(cacheKey, event);
                return event;
            }

            lock = redisService.getLock(cacheKey);

            if (!lock.tryLock(1, 2, TimeUnit.SECONDS)) {
                log.warn("Unable to acquire lock for key: {}", key);
                return null;
            }

            // Double check after acquiring lock
            event = redisCacheService.get(cacheKey, Event.class);
            if (event != null) {
                log.info("Retrieved from Redis cache after lock acquisition: {}", event);
                eventCacheLocalService.put(cacheKey, event);
                return event;
            }

            event = eventService.getById(key).orElse(null);
            if (event != null) {
                log.info("Retrieved from database: {}", event);
                redisCacheService.set(cacheKey, event, Duration.ofMinutes(5));
                eventCacheLocalService.put(cacheKey, event);
            }

            return event;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Lock acquisition was interrupted for key: {}", key, e);
            throw new RuntimeException("Failed to acquire lock", e);
        } catch (Exception e) {
            log.error("Error while retrieving event for key: {}", key, e);
            throw new RuntimeException("Failed to get event", e);
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                try {
                    lock.unlock();
                    log.debug("Lock released for key: {}", key);
                } catch (IllegalMonitorStateException e) {
                    log.warn("Failed to release lock - possible expiration for key: {}", key, e);
                }
            }
        }
    }

    @Override
    public void saveEvent(Event event) {

    }
}
