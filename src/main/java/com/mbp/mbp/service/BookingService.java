package com.mbp.mbp.service;

import com.mbp.mbp.dto.LockSeatRequest;
import com.mbp.mbp.enums.BookingStatus;
import com.mbp.mbp.model.Booking;
import com.mbp.mbp.producer.BookingEventProducer;
import com.mbp.mbp.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatLockService seatLockService;
    private final BookingRepository bookingRepository;
    private final BookingEventProducer eventProducer;

    public void lockSeats(LockSeatRequest request) {
        seatLockService.lockSeats(
                request.getShowId(),
                request.getSeatIds(),
                request.getUserId()
        );

        Booking booking = Booking.builder()
                .showId(request.getShowId())
                .userId(request.getUserId())
                .status(BookingStatus.PENDING)
                .seatIds(request.getSeatIds())
                .build();

        bookingRepository.save(booking);

        eventProducer.publishBookingCreated(booking.getId());
    }

    @Transactional
    public void cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Booking not found"));

        // Idempotent handling
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return;
        }

        // Business rule
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending bookings can be cancelled");
        }

        //Update booking state
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        //Unlock seats
        seatLockService.unlockSeats(
                booking.getShowId(),
                booking.getSeatIds()
        );

        //Notify downstream systems
        eventProducer.publishBookingFailed(booking.getId());
    }
}
