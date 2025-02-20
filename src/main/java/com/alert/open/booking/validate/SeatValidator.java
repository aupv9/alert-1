package com.alert.open.booking.validate;

import com.alert.open.booking.EventBookingException;
import com.alert.open.entity.Event;

public interface SeatValidator {
    void validate(Event event, int seatNumber) throws EventBookingException;
}
