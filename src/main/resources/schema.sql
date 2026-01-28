CREATE TABLE shows (
                       id BIGSERIAL PRIMARY KEY,
                       movie_name VARCHAR(255) NOT NULL,
                       show_time TIMESTAMP NOT NULL
);

CREATE TABLE seats (
                       id BIGSERIAL PRIMARY KEY,
                       show_id BIGINT NOT NULL,
                       seat_number VARCHAR(10) NOT NULL,
                       status VARCHAR(20) NOT NULL,
                       CONSTRAINT fk_seat_show
                           FOREIGN KEY (show_id)
                               REFERENCES shows(id)
);

CREATE TABLE bookings (
                          id BIGSERIAL PRIMARY KEY,
                          show_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_booking_show
                              FOREIGN KEY (show_id)
                                  REFERENCES shows(id)
);

CREATE INDEX idx_seats_show_id ON seats(show_id);
CREATE INDEX idx_bookings_show_id ON bookings(show_id);
