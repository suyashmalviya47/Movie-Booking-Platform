package com.mbp.mbp.consumer;

import com.mbp.mbp.enums.BookingStatus;
import com.mbp.mbp.model.Booking;
import com.mbp.mbp.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final BookingRepository bookingRepository;

    @KafkaListener(topics = "payment.completed", groupId = "booking-service")
    public void handlePayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }
}
