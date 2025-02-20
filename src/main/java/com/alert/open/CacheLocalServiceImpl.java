package com.alert.open;

import com.alert.open.entity.Event;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class CacheLocalServiceImpl implements CacheLocalService<Event>{

    private static final Cache<String, Event> EVENT_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .concurrencyLevel(8)
            .recordStats()
            .build();

    @Override
    public Event getEventById(String eventId) {
        if (EVENT_CACHE.getIfPresent(eventId) != null) {
            return EVENT_CACHE.getIfPresent(eventId);
        }
        return null;
    }

    @Override
    public void put(String eventId, Event event) {
        EVENT_CACHE.put(eventId, event);
    }
}
