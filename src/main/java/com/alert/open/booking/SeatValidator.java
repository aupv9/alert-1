package com.alert.open.booking;

import com.alert.open.entity.Event;

public interface SeatValidator {
    void validate(Event event, int seatNumber) throws EventBookingException;
}
