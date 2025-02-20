package com.alert.open.booking.validate;

import com.alert.open.EventStatus;
import com.alert.open.booking.EventBookingException;
import com.alert.open.booking.StatusValidator;
import com.alert.open.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventStatusValidator implements StatusValidator {
    @Override
    public void validate(Event event) throws EventBookingException {
        if (event.getStatus() != EventStatus.ACTIVE) {
            throw new EventBookingException(
                    String.format("Event is not active. Current status: %s", event.getStatus())
            );
        }
    }
}
