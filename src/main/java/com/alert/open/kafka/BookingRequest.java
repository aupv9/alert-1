package com.alert.open.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
    private String agentId;
    private Long eventId;
    private String customerId;
    private String bookingTime;
    private String status;
    private String ticketType;
    private int amount;
}

