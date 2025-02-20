package com.alert.open.booking.validate;

import com.alert.open.booking.EventBookingException;
import com.alert.open.booking.TimeValidator;
import com.alert.open.entity.Event;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventTimeValidator implements TimeValidator {
    @Override
    public void validate(Event event) throws EventBookingException {
        LocalDateTime now = LocalDateTime.now();

        // Kiểm tra xem event đã kết thúc chưa
//        if (event.getEventDate().isBefore(now)) {
//            throw new EventBookingException("Event has already ended");
//        }

        // Kiểm tra xem đã đến deadline đặt vé chưa
//        if (event.getBookingDeadline() != null &&
//                event.getBookingDeadline().isBefore(now)) {
//            throw new EventBookingException(
//                    EventBookingError.EVENT_EXPIRED,
//                    "Booking deadline has passed"
//            );
//        }
    }
}
