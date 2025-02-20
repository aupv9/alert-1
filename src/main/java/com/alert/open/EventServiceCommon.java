package com.alert.open;

public interface EventServiceCommon<T, K> {
    T getEventById(K key);
    void saveEvent(T event);
}
