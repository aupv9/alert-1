package com.alert.open.kafka;

import com.alert.open.booking.BookingManager;
import com.alert.open.booking.BookingOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingProcessor {
    private final BookingManager bookingManager;
    private final BookingOrchestrator bookingOrchestrator;
    private final RedissonClient redissonClient;

    private static final String BOOKING_LOCK_PREFIX = "booking:lock:event:";
    private static final int LOCK_WAIT_TIME = 10;
    private static final int LOCK_LEASE_TIME = 30;

    @KafkaListener(
            topics = "agent-bookings",
            groupId = "booking-processor-group",
            concurrency = "3",
            batch = "true",
            properties = {
                    "max.poll.records=100",
                    "fetch.min.bytes=1048576",
                    "fetch.max.wait.ms=500"
            }
    )
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processBookings(
            @Payload List<BookingRequest> bookingRequests,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition
    ) {
        Map<Long, List<BookingRequest>> requestsByEvent = bookingRequests.stream()
                .collect(Collectors.groupingBy(BookingRequest::getEventId));

        requestsByEvent.forEach(this::processEventBookings);
    }

    private void processEventBookings(Long eventId, List<BookingRequest> requests) {
        String lockKey = BOOKING_LOCK_PREFIX + eventId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS);

            if (!isLocked) {
                log.warn("Failed to acquire lock for event {}, {} requests will be retried",
                        eventId, requests.size());
                handleLockFailure(eventId, requests);
                return;
            }

            log.info("Processing {} bookings for event: {}", requests.size(), eventId);
            var event = bookingOrchestrator.getEventWithCache(eventId);

            // Chuyển đổi thành BookingRequest
            List<com.alert.open.request.BookingRequest> bookingRequests = requests.stream()
                    .map(request -> {
                        var bookingRequest = new com.alert.open.request.BookingRequest();
                        bookingRequest.setEmail(request.getCustomerId());
                        bookingRequest.setNumberOfSeats(request.getAmount());
                        return bookingRequest;
                    })
                    .toList();

            // Thực hiện batch insert
            bookingManager.createBookingsBatch(event, bookingRequests);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Lock acquisition was interrupted for event {}", eventId);
        } catch (Exception e) {
            log.error("Error processing bookings for event {}: {}", eventId, e.getMessage());
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private void handleLockFailure(Long eventId, List<BookingRequest> requests) {
        // Implement retry logic cho các requests bị fail do lock
        log.warn("Lock acquisition failed for event {}, {} requests will be retried",
                eventId, requests.size());
    }

}