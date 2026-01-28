package com.mbp.mbp.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingEventProducer {

    private final KafkaTemplate<String, Long> kafkaTemplate;

    public void publishBookingCreated(Long bookingId) {
        kafkaTemplate.send("booking.created", bookingId);
    }
}
