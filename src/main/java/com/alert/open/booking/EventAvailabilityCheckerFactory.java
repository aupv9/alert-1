package com.alert.open.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EventAvailabilityCheckerFactory {
    private final Map<EventType, EventAvailabilityChecker> checks;


    @Autowired
    public EventAvailabilityCheckerFactory(DefaultEventAvailabilityChecker eventAvailabilityCheckers) {
        this.checks = new EnumMap<>(EventType.class);
        checks.put(EventType.STANDARD, eventAvailabilityCheckers);
    }
    public EventAvailabilityChecker getChecker(EventType type) {
        return checks.getOrDefault(type, checks.get(EventType.STANDARD));
    }
}
