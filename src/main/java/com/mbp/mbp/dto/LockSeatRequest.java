package com.mbp.mbp.dto;

import lombok.Data;

import java.util.List;

@Data
public class LockSeatRequest {
    private Long showId;
    private List<Long> seatIds;
    private Long userId;
}
