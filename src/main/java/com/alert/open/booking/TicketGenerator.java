package com.alert.open.booking;

import com.alert.open.entity.Booking;
import com.alert.open.entity.Ticket;

public interface TicketGenerator {
    Ticket generateTicket(Booking booking);
}
