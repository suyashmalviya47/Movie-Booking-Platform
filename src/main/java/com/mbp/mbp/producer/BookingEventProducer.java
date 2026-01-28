package com.mbp.mbp.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingEventProducer {


    private static final String BOOKING_CREATED_TOPIC = "booking.created";
    private static final String BOOKING_FAILED_TOPIC = "booking.failed";
    private final KafkaTemplate<String, Long> kafkaTemplate;

    public void publishBookingCreated(Long bookingId) {
        kafkaTemplate.send(BOOKING_CREATED_TOPIC, bookingId);
    }

    public void publishBookingFailed(Long bookingId) {
        kafkaTemplate.send(BOOKING_FAILED_TOPIC, bookingId);
    }
}
