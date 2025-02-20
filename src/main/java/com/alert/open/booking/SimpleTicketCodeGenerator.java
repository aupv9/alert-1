package com.alert.open.booking;


import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SimpleTicketCodeGenerator implements TicketCodeGenerator {
    @Override
    public String generateCode() {
        return "TIX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
