package com.alert.open.result;

import com.alert.open.entity.Booking;
import com.alert.open.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResult {
    private Booking books;
    private List<Ticket> tickets;
}
