package com.cgi.app.service;

import com.cgi.app.entity.seat.CinemaHallEntity;
import com.cgi.app.entity.seat.SeatEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.cgi.app.entity.seat.SeatEntity.OccupationStatus.SELECTED;

@Service
public class SeatingPlanService {

    private final CinemaHallEntity cinemaHall = new CinemaHallEntity();


    /**
     * Generate a random occupation status for all seats.
     * Then find and select the best available set of neighbouring seats.
     * Neighbouring seats must be next to each other and in the same row.
     *
     * @param ticketCount amount of seats to be selected
     * @return list containing json data about all seats
     */
    public Optional<List<Map<String, String>>> getSeatingPlanWithNeighbouringSeats(int ticketCount) {
        List<SeatEntity> seats = cinemaHall.getSeats();
        seats.forEach(SeatEntity::setRandomOccupationStatus);  // generate random occupation status for each seat

        HashMap<List<SeatEntity>, Integer> neighbouringSeatsCombinations = new HashMap<>();

        for (int firstSeatIndex = 0; firstSeatIndex < seats.size() + 1 - ticketCount; firstSeatIndex++) {

            List<SeatEntity> potentialNeighbouringSeats = seats.subList(firstSeatIndex, firstSeatIndex + ticketCount);

            if (isValidNeighbouringSeatsCombination(potentialNeighbouringSeats)) {  // all are free and in the same row
                int combinedDistanceFromCenter = potentialNeighbouringSeats.stream()
                        .mapToInt(SeatEntity::getDistanceFromCenter)
                        .sum();
                neighbouringSeatsCombinations.put(potentialNeighbouringSeats, combinedDistanceFromCenter);
            }
        }

        Optional<List<SeatEntity>> bestNeighbouringSeatsCombination = neighbouringSeatsCombinations
                .keySet()
                .stream()
                .min(Comparator
                        .comparing(neighbouringSeatsCombinations::get));  // compare by combined distanceFromCenter

        if (bestNeighbouringSeatsCombination.isPresent()) {
            bestNeighbouringSeatsCombination.get().forEach(x -> x.setOccupationStatus(SELECTED));  // select the seats
            return Optional.of(seats.stream()
                    .map(SeatEntity::toJson)
                    .toList()
            );
        }
        return Optional.empty();
    }

    /**
     * Check whether all neighbouring seats are free and in the same row.
     */
    private boolean isValidNeighbouringSeatsCombination(List<SeatEntity> seats) {
        boolean allSeatsAreFree = seats.stream()
                .allMatch(SeatEntity::isFree);
        boolean allSeatsAreInSameRow = seats.stream()
                .mapToInt(SeatEntity::getRowNumber)
                .distinct()
                .toArray().length == 1;
        return allSeatsAreFree && allSeatsAreInSameRow;
    }


    /**
     * Select the best available seats, even when they are not neighbouring.
     * TODO: Try to get a smaller amount of neighbouring seats instead of any (closest to center) seats.
     *
     * @param ticketCount amount of seats to be selected
     * @return list containing json data about all seats
     */
    public Optional<List<Map<String, String>>> getAnySeatingPlan(int ticketCount) {
        List<SeatEntity> seats = cinemaHall.getSeats();

        List<SeatEntity> result = seats.stream()
                .filter(SeatEntity::isFree)
                .sorted(Comparator.comparing(SeatEntity::getDistanceFromCenter))
                .toList();
        if (result.size() >= ticketCount) {
            // select best seats (closest to the centre)
            result.subList(0, ticketCount).forEach(x -> x.setOccupationStatus(SELECTED));
            return Optional.of(seats.stream()
                    .map(SeatEntity::toJson)
                    .toList());
        }
        return Optional.empty();
    }

}
