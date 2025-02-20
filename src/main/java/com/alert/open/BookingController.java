package com.alert.open;

import com.alert.open.booking.BookingOrchestrator;
import com.alert.open.entity.Event;
import com.alert.open.exception.ConcurrentBookingException;
import com.alert.open.exception.EventNotFoundException;
import com.alert.open.request.BookingRequest;
import com.alert.open.response.ApiResponse;
import com.alert.open.result.BookingResult;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class BookingController {


    private final EventServiceCommonImpl eventServiceCommon;

    private final BookingOrchestrator bookingOrchestrator;

    @GetMapping("/greeting")
    @RateLimiter(name = "backendA", fallbackMethod = "fallBackA")
    public ResponseEntity<String> greeting() {
        return ResponseEntity.ok("Hello");
    }

    // Cách 1: Fallback với RequestNotPermitted
    public ResponseEntity<String> fallBackA(RequestNotPermitted exception) {

        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Rate limit exceeded - Fallback Response");
    }

    // Cách 2: Fallback với Exception (capture tất cả exceptions)
    public ResponseEntity<String> fallBackA(Exception exception) {
        if (exception instanceof RequestNotPermitted) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Rate limit exceeded - Fallback Response");
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred");
    }

    @GetMapping("/health")
    public ResponseEntity<Object> healthCheck() {
        return ResponseEntity.ok().body("UP");
    }


    @GetMapping("/event/{id}")
    public ResponseEntity<Event> book(@PathVariable("id")String eventId) {
        return ResponseEntity.ok().body(eventServiceCommon.getEventById(eventId));
    }

    @PostMapping("/events/{eventId}")
    public ResponseEntity<ApiResponse<BookingResult>> createBooking(
            @PathVariable("eventId") Long eventId,
            @RequestBody BookingRequest request
    ) {
        try {
            log.info("Received booking request for event ID: {} with request: {}", eventId, request);
            BookingResult result = bookingOrchestrator.processBooking(eventId, request);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (ConcurrentBookingException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (EventNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An unexpected error occurred during booking"));
        }
    }
}


