package com.mbp.mbp.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private boolean success;
    private String message;
}