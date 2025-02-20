package com.alert.open.booking;

import com.alert.open.entity.Event;

public interface EventAvailabilityChecker {
    void checkAvailability(Event event, int requestedSeats) throws EventBookingException;
}
