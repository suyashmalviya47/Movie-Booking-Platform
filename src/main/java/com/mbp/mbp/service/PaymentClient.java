package com.mbp.mbp.service;

import com.mbp.mbp.dto.PaymentRequest;
import com.mbp.mbp.dto.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentClient {

    @CircuitBreaker(name = "paymentService", fallbackMethod = "fallback")
    public PaymentResponse initiatePayment(PaymentRequest request) {
        return new PaymentResponse(true, "SUCCESS");
    }

    public PaymentResponse fallback(PaymentRequest request, Exception e) {
        return new PaymentResponse(false, "FAILED");
    }
}
