package com.alert.open.booking.validate;

import com.alert.open.booking.EventBookingException;
import com.alert.open.booking.SeatValidator;
import com.alert.open.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class SeatAvailabilityValidator implements SeatValidator {
    @Override
    public void validate(Event event, int requestedSeats) throws EventBookingException {
        // Kiểm tra số lượng ghế yêu cầu hợp lệ
        if (requestedSeats <= 0) {
            throw new EventBookingException(
//                    EventBookingError.INVALID_SEAT_COUNT,
                    "Number of requested seats must be greater than 0"
            );
        }

        // Kiểm tra số lượng ghế còn trống
        if (event.getAvailableSeats() < requestedSeats) {
            throw new EventBookingException(
//                    EventBookingError.INSUFFICIENT_SEATS,
                    String.format(
                            "Not enough seats available. Requested: %d, Available: %d",
                            requestedSeats,
                            event.getAvailableSeats()
                    )
            );
        }

        // Kiểm tra giới hạn đặt chỗ cho mỗi booking (nếu có)
//        if (event.getMaxSeatsPerBooking() != null &&
//                requestedSeats > event.getMaxSeatsPerBooking()) {
//            throw new EventBookingException(
//                    EventBookingError.INVALID_SEAT_COUNT,
//                    String.format(
//                            "Exceeds maximum seats per booking. Maximum allowed: %d",
//                            event.getMaxSeatsPerBooking()
//                    )
//            );
//        }
    }
}
