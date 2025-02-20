package com.alert.open.booking.validate;

import com.alert.open.booking.EventBookingException;
import com.alert.open.entity.Event;

public interface StatusValidator {
    void validate(Event status) throws EventBookingException;
}
