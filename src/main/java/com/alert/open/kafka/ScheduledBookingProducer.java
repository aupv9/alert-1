package com.alert.open.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Service
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledBookingProducer {
    private final KafkaTemplate<String, BookingRequest> kafkaTemplate;
    private static final String BOOKING_TOPIC = "agent-bookings";
    private static final List<String> AGENT_IDS = Arrays.asList("AGENT001", "AGENT002", "AGENT003");
    private static final List<String> TICKET_TYPES = Arrays.asList("ECONOMY", "BUSINESS", "FIRST_CLASS");
    private final Random random = new Random();
    private static final int BATCH_SIZE = 200;

    @Scheduled(fixedRate = 60000) // Changed to run every minute to prevent overwhelming
    public void generateBookings() {
        List<CompletableFuture<Void>> futures = IntStream.range(0, BATCH_SIZE)
                .mapToObj(i -> createAndSendBooking())
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> log.info("Completed sending batch of {} bookings", BATCH_SIZE))
                .exceptionally(throwable -> {
                    log.error("Error in batch processing: {}", throwable.getMessage());
                    return null;
                });
    }

    private CompletableFuture<Void> createAndSendBooking() {
        BookingRequest booking = createRandomBooking();
        return sendBooking(booking);
    }

    private BookingRequest createRandomBooking() {
        String agentId = AGENT_IDS.get(random.nextInt(AGENT_IDS.size()));
        String ticketType = TICKET_TYPES.get(random.nextInt(TICKET_TYPES.size()));

        return BookingRequest.builder()
                .eventId(random.nextLong(1, 2))
                .agentId(agentId)
                .customerId(UUID.randomUUID().toString())
                .status("PENDING")
                .ticketType(ticketType)
                .amount(random.nextInt(1, 10))
                .build();
    }

    private CompletableFuture<Void> sendBooking(BookingRequest booking) {
        return CompletableFuture.runAsync(() -> {
            try {
                kafkaTemplate.send(BOOKING_TOPIC, booking.getAgentId(), booking)
                        .thenAccept(result -> {
                            log.info("Generated booking sent successfully: agentId={}, bookingId={}, amount={}",
                                    booking.getAgentId(),
                                    booking.getEventId(),
                                    booking.getAmount());
                        })
                        .exceptionally(throwable -> {
                            log.error("Failed to send generated booking: {}", throwable.getMessage());
                            return null;
                        });
            } catch (Exception e) {
                log.error("Error while sending generated booking: {}", e.getMessage());
            }
        });
    }
}