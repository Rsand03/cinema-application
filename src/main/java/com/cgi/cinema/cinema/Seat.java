package com.cgi.cinema.cinema;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.cgi.cinema.cinema.Seat.OccupationStatus.FREE;
import static com.cgi.cinema.cinema.Seat.OccupationStatus.OCCUPIED;

public class Seat {

    private static final double SEAT_BEING_OCCUPIED_PROBABILITY = 0.5;

    private final int rowNumber;
    private final int seatNumber;
    private final int distanceFromCenter;
    private OccupationStatus occupationStatus = OccupationStatus.FREE;

    public enum OccupationStatus {
        OCCUPIED, FREE, SELECTED
    }

    protected Seat(int rowNumber, int seatNumber, int distanceFromCenter) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.distanceFromCenter = distanceFromCenter;
    }

    public boolean isFree() {
        return occupationStatus.equals(FREE);
    }

    public void setOccupationStatus(OccupationStatus status) {
        this.occupationStatus = status;
    }

    public void setRandomOccupationStatus() {
        this.occupationStatus = getRandomOccupationStatus();
    }

    private OccupationStatus getRandomOccupationStatus() {
        Random randomObject = new Random();
        int randomInt = randomObject.nextInt(0, 100);
        if (randomInt > 100 * SEAT_BEING_OCCUPIED_PROBABILITY) {
            return FREE;
        };
        return OCCUPIED;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getDistanceFromCenter() {
        return distanceFromCenter;
    }

    public String getOccupationStatus() {
        return occupationStatus.toString();
    }

    public Map<String, String> toJson() {
        HashMap<String, String> seatHashMap = new HashMap<>();
        seatHashMap.put("seatNumber", String.valueOf(seatNumber));
        seatHashMap.put("occupationStatus", getOccupationStatus());
        return seatHashMap;
    }
}
