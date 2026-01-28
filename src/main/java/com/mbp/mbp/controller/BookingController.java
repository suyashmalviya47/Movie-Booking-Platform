package com.mbp.mbp.controller;

import com.mbp.mbp.dto.LockSeatRequest;
import com.mbp.mbp.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/lock")
    public ResponseEntity<String> lockSeats(@RequestBody LockSeatRequest request) {
        bookingService.lockSeats(request);
        return ResponseEntity.ok("Seats locked");
    }
}
