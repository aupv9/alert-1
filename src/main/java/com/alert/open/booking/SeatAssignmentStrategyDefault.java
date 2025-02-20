package com.alert.open.booking;

import com.alert.open.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class SeatAssignmentStrategyDefault implements SeatAssignmentStrategy{
    @Override
    public String assignSeat(Event event, int seatIndex) {
        return "";
    }
}
