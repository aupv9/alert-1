package com.alert.open.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ với Booking
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // Thông tin vé
    private String ticketCode; // Mã vé độc nhất
    private String seatNumber; // Số ghế (nếu có)
    private BigDecimal price;  // Giá vé
    private LocalDateTime issueDate; // Ngày phát hành
    private TicketStatus status = TicketStatus.ACTIVE;

    // QR code hoặc barcode cho vé
    private String ticketBarcode;

    @PrePersist
    protected void onCreate() {
        issueDate = LocalDateTime.now();
//        // Tạo mã vé độc nhất
//        ticketCode = generateTicketCode();
//        // Tạo barcode
//        ticketBarcode = generateBarcode();
    }

//    private String generateTicketCode() {
//        return "TIX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
//    }
//
//    private String generateBarcode() {
//        return UUID.randomUUID().toString();
//    }
}

