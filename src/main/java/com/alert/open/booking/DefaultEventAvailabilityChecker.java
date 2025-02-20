package com.alert.open.booking;

import com.alert.open.entity.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DefaultEventAvailabilityChecker implements EventAvailabilityChecker{

    private final TimeValidator timeValidator;
    private final StatusValidator statusValidator;
    private final SeatValidator seatValidator;

    @Override
    public void checkAvailability(Event event, int requestedSeats) throws EventBookingException {

        timeValidator.validate(event);
        statusValidator.validate(event);
        seatValidator.validate(event, requestedSeats);
    }
}
