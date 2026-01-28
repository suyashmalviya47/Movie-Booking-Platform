package com.mbp.mbp.service;

import com.mbp.mbp.enums.BookingStatus;
import com.mbp.mbp.model.Booking;
import com.mbp.mbp.producer.BookingEventProducer;
import com.mbp.mbp.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingExpiryScheduler {

    private final BookingRepository bookingRepository;
    private final StringRedisTemplate redisTemplate;
    private final BookingEventProducer eventProducer;
    private final SeatLockService seatLockService;


    // Payment window: 5 minutes
    private static final Duration PAYMENT_TIMEOUT = Duration.ofMinutes(5);

    @Scheduled(fixedDelayString = "${booking.expiry.delay-ms}")
    @Transactional
    public void expireUnpaidBookings() {

        LocalDateTime expiryTime =
                LocalDateTime.now().minus(PAYMENT_TIMEOUT);

        List<Booking> expiredBookings =
                bookingRepository.findExpiredPendingBookings(expiryTime);

        for (Booking booking : expiredBookings) {

            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);

            // Release Redis locks
            seatLockService.unlockSeats(booking.getId(), booking.getSeatIds());

            // Emit event for downstream systems
            eventProducer.publishBookingFailed(booking.getId());

            log.info("Expired booking {}", booking.getId());
        }
    }

}
