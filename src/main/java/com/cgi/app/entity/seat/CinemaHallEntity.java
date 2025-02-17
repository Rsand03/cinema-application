package com.cgi.app.entity.seat;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.cgi.app.util.Constants.CENTRE_SEAT_COLUMN_NUMBER;
import static com.cgi.app.util.Constants.CENTRE_SEAT_ROW_NUMBER;
import static com.cgi.app.util.Constants.SEAT_COLUMNS_COUNT;
import static com.cgi.app.util.Constants.TOTAL_SEATS_COUNT;

@Getter
public class CinemaHallEntity {

    private final List<SeatEntity> seats = new ArrayList<>();

    public CinemaHallEntity() {
        generateSeats();
    }

    /**
     * Generate a seating plan for the cinema.
     * All seats have a seatNumber and a rowNumber.
     * */
    private void generateSeats() {

        for (int i = 0; i < TOTAL_SEATS_COUNT; i++) {
            int columnNumber = (i % SEAT_COLUMNS_COUNT) + 1; // columnNumber indexing starts at 1
            int rowNumber = (i / SEAT_COLUMNS_COUNT) + 1;  // rowNumber indexing starts at 1

            int distanceFromCenterColumn = Math.abs(CENTRE_SEAT_COLUMN_NUMBER - columnNumber);
            int distanceFromCenterRow = Math.abs(CENTRE_SEAT_ROW_NUMBER - rowNumber);
            int distanceFromCenter = distanceFromCenterColumn + distanceFromCenterRow;

            // seatNumber indexing starts at 1
            SeatEntity newSeat = new SeatEntity(rowNumber, i + 1, distanceFromCenter);
            seats.add(newSeat);
        }
    }

}
