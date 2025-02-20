package com.alert.open.booking;

import com.alert.open.EventRepository;
import com.alert.open.RedisService;
import com.alert.open.entity.Booking;
import com.alert.open.entity.Event;
import com.alert.open.entity.Ticket;
import com.alert.open.request.BookingRequest;
import com.alert.open.result.BookingResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookingOrchestrator {
    private final BookingManager bookingManager;
    private final TicketGenerator ticketGenerator;
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final EventAvailabilityCheckerFactory checkerFactory;
    private final RedisService redisService;

    private static final String EVENT_CACHE_PREFIX = "event:";


    @Transactional
    public BookingResult processBooking(Long eventId, BookingRequest request) throws Exception {

        return redisService.executeWithLock(
                String.valueOf(eventId),
                1000L,
                1000L,
                () -> {
                    try {
                        return executeBookingProcess(eventId, request);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private BookingResult executeBookingProcess(Long eventId, BookingRequest request) throws Exception {

        log.info("Received booking request for event ID: {} with request: {}", eventId, request);
        // Try to get event from cache first
        var event = getEventWithCache(eventId);

        // Check availability
//        EventAvailabilityChecker checker = checkerFactory.getChecker(EventType.STANDARD);

//        checker.checkAvailability(event, request.getNumberOfSeats());

        // Create booking
//        Booking booking = bookingManager.createBooking(event, request);

        // Generate tickets
//        List<Ticket> tickets = new ArrayList<>();
//        for (int i = 0; i < request.getNumberOfSeats(); i++) {
//            tickets.add(ticketGenerator.generateTicket(booking));
//        }
//        tickets = ticketRepository.saveAll(tickets);
//
//        // Update available seats
//        event.setAvailableSeats(event.getAvailableSeats() - request.getNumberOfSeats());
//        event = eventRepository.save(event);
//
//        // Update cache
//        updateEventCache(event);

//        return new BookingResult(booking, tickets);
        return null;
    }

    public Event getEventWithCache(Long eventId) throws Exception {
        String cacheKey = EVENT_CACHE_PREFIX + eventId;
        Event event = redisService.getValue(cacheKey, Event.class);

        if (event == null) {
            event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new Exception("Event not found with id: " +eventId));
            redisService.setValue(cacheKey, event, Duration.ofMinutes(5));
        }

        return event;
    }

    private void updateEventCache(Event event) {
        redisService.setValue(EVENT_CACHE_PREFIX + event.getId(), event, Duration.ofMinutes(5));
    }
}
