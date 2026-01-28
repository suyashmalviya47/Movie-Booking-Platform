package com.mbp.mbp.controller;

import com.mbp.mbp.dto.LockSeatRequest;
import com.mbp.mbp.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<String> cancelBooking(
            @PathVariable Long bookingId) {

        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}
