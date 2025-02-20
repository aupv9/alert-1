package com.alert.open;


public interface CacheLocalService<T> {
    T getEventById(String eventId);
    void put(String key, T item);
}
