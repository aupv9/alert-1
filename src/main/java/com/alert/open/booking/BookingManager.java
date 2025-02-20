package com.alert.open.booking;

import com.alert.open.entity.Booking;
import com.alert.open.entity.Event;
import com.alert.open.request.BookingRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class BookingManager {
    private final EntityManager entityManager;

    private static final int BATCH_SIZE = 100;

    @Transactional
    public List<Booking> createBookingsBatch(Event event, List<BookingRequest> requests) {
        List<Booking> bookings = new ArrayList<>();

        for (int i = 0; i < requests.size(); i++) {
            BookingRequest request = requests.get(i);
            Booking booking = new Booking();
            booking.setEventId(event.getId());
            booking.setCustomerEmail(request.getEmail());
            booking.setNumberOfSeats(request.getNumberOfSeats());

            entityManager.persist(booking);
            bookings.add(booking);

            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();

        return bookings;
    }
}