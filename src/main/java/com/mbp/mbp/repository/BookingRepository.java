package com.mbp.mbp.repository;

import com.mbp.mbp.model.Booking;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
       SELECT b FROM Booking b
       WHERE b.status = 'PENDING'
       AND b.createdAt < :expiryTime
    """)
    List<Booking> findExpiredPendingBookings(
            @Param("expiryTime") LocalDateTime expiryTime
    );
}
