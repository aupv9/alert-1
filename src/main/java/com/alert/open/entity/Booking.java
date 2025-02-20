package com.alert.open.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;

    @Column
    private String customerName;

    @Column
    private String customerEmail;

    @Column(nullable = false)
    private Integer numberOfSeats;

    @Column
    private LocalDateTime bookingTime;

    @Column
    private String bookingStatus = "PENDING"; // Có thể là "CONFIRMED", "CANCELLED", "PENDING"


    @PrePersist
    protected void onCreate() {
        bookingTime = LocalDateTime.now();
    }
}