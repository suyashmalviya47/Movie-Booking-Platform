package com.mbp.mbp.model;

import com.mbp.mbp.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    private Long showId;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
