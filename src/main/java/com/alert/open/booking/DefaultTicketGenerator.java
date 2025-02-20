package com.alert.open.booking;

import com.alert.open.entity.Booking;
import com.alert.open.entity.Ticket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DefaultTicketGenerator implements TicketGenerator{
    private final TicketCodeGenerator codeGenerator;
    private final SeatAssignmentStrategy seatAssigner;

    @Override
    public Ticket generateTicket(Booking booking) {
        Ticket ticket = new Ticket();
        ticket.setBooking(booking);
//        ticket.setPrice(booking.getEvent().getPrice());
        ticket.setTicketCode(codeGenerator.generateCode());

//        if (booking.getEvent().hasAssignedSeating()) {
//            ticket.setSeatNumber(seatAssigner.assignSeat(booking.getEvent(), booking.getTickets().size()));
//        }

        return ticket;
    }
}
