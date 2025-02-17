package com.cgi.app.entity.seat;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.cgi.app.entity.seat.SeatEntity.OccupationStatus.FREE;
import static com.cgi.app.entity.seat.SeatEntity.OccupationStatus.OCCUPIED;
import static com.cgi.app.util.Constants.SEAT_BEING_OCCUPIED_IN_SEATING_PLAN_PROBABILITY;

@Getter
@Setter
public class SeatEntity {

    private final int rowNumber;
    private final int seatNumber;
    private final int distanceFromCenter;
    private OccupationStatus occupationStatus = OccupationStatus.FREE;
    private final Random random = new Random();


    public enum OccupationStatus {
        OCCUPIED, FREE, SELECTED
    }

    /**
     * Initialize Seat.
     *
     * @param rowNumber          row number
     * @param seatNumber         seat number
     * @param distanceFromCenter seat's distance from the centre point of the cinema hall
     */
    public SeatEntity(int rowNumber, int seatNumber, int distanceFromCenter) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.distanceFromCenter = distanceFromCenter;
    }

    /**
     * Check if the seat is free/available.
     *
     * @return boolean
     */
    public boolean isFree() {
        return occupationStatus.equals(FREE);
    }

    /**
     * Set the occupation status randomly to FREE or OCCUPIED.
     */
    public void setRandomOccupationStatus() {
        this.occupationStatus = getRandomOccupationStatus();
    }

    /**
     * Generate random occupation status based on SEAT_BEING_OCCUPIED_PROBABILITY constant.
     *
     * @return randomized occupation status
     */
    private OccupationStatus getRandomOccupationStatus() {
        int randomInt = random.nextInt(0, 100);
        if (randomInt > 100 * SEAT_BEING_OCCUPIED_IN_SEATING_PLAN_PROBABILITY) {
            return FREE;
        }
        return OCCUPIED;
    }

    public String getOccupationStatus() {
        return occupationStatus.toString();
    }

    /**
     * Get seat data in json format.
     *
     * @return seat number and occupation status in json format
     */
    public Map<String, String> toJson() {
        HashMap<String, String> seatHashMap = new HashMap<>();
        seatHashMap.put("seatNumber", String.valueOf(seatNumber));
        seatHashMap.put("occupationStatus", getOccupationStatus());
        return seatHashMap;
    }
}
