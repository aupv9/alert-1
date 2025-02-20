package com.alert.open.booking;

import com.alert.open.entity.Event;

public interface SeatAssignmentStrategy {
    String assignSeat(Event event, int seatIndex);
}
