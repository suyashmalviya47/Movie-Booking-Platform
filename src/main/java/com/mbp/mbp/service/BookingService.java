package com.mbp.mbp.service;

import com.mbp.mbp.dto.LockSeatRequest;
import com.mbp.mbp.enums.BookingStatus;
import com.mbp.mbp.model.Booking;
import com.mbp.mbp.producer.BookingEventProducer;
import com.mbp.mbp.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .build();

        bookingRepository.save(booking);
        eventProducer.publishBookingCreated(booking.getId());
    }
}
