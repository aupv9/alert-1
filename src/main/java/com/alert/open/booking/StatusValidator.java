package com.alert.open.booking;

import com.alert.open.entity.Event;

public interface StatusValidator {
    void validate(Event status) throws EventBookingException;
}
