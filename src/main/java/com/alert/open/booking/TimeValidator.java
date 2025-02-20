package com.alert.open.booking;

import com.alert.open.entity.Event;

public interface TimeValidator {
    void validate(Event event) throws EventBookingException;
}
