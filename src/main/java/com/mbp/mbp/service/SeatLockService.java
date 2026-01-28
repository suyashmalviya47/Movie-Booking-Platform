package com.mbp.mbp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SeatLockService {

    private final StringRedisTemplate redisTemplate;
    private final ExecutorService bookingExecutor;
    private static final long TTL = 5;

    public void lockSeats(Long showId, List<Long> seatIds, Long userId) {

        List<Callable<Boolean>> tasks = seatIds.stream()
                .map(seatId -> (Callable<Boolean>) () ->
                        redisTemplate.opsForValue()
                                .setIfAbsent(buildKey(showId, seatId),
                                        userId.toString(),
                                        TTL,
                                        TimeUnit.MINUTES))
                .toList();

        try {
            for (Future<Boolean> f : bookingExecutor.invokeAll(tasks)) {
                if (Boolean.FALSE.equals(f.get())) {
                    throw new RuntimeException("Seat already locked");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void unlockSeats(Long showId, List<Long> seatIds) {

        if (seatIds == null || seatIds.isEmpty()) {
            return;
        }

        List<String> keys = seatIds.stream()
                .map(seatId -> buildKey(showId, seatId))
                .toList();

        redisTemplate.delete(keys);
    }

    private String buildKey(Long showId, Long seatId) {
        return "lock:show:" + showId + ":seat:" + seatId;
    }
}
