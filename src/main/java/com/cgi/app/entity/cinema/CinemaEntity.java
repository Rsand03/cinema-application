package com.cgi.app.entity.cinema;

import com.cgi.app.entity.movie.MovieEntity;
import com.cgi.app.entity.movie.MovieSessionEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.cgi.app.util.Constants.CENTRE_SEAT_COLUMN_NUMBER;
import static com.cgi.app.util.Constants.CENTRE_SEAT_ROW_NUMBER;
import static com.cgi.app.util.Constants.SEAT_COLUMNS_COUNT;
import static com.cgi.app.util.Constants.TOTAL_SEATS_COUNT;
import static com.cgi.app.util.Constants.TOTAL_AVAILABLE_MOVIE_SESSIONS_COUNT;
import static com.cgi.app.util.Constants.AVAILABLE_MOVIE_TITLES;
import static com.cgi.app.entity.cinema.SeatEntity.OccupationStatus.SELECTED;


public class CinemaEntity {

    private final List<MovieEntity> movies = new ArrayList<>();
    private final List<MovieSessionEntity> sessions = new ArrayList<>();
    private final List<SeatEntity> seats = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Initialize Cinema class by generating movies and a seating plan.
     * */
    public CinemaEntity() {
        generateMovies();
        generateSessions(TOTAL_AVAILABLE_MOVIE_SESSIONS_COUNT);
        generateSeats();
    }

    /**
     * Generate and save new movies with randomized parameters.
     * */
    public void generateMovies() {
        for (String movieTitle : AVAILABLE_MOVIE_TITLES) {
            movies.add(new MovieEntity(movieTitle));
        }
    }

    /**
     * Generate and save new sessions with randomized parameters.
     * @param amountOfSessions amount of sessions to be created
     * */
    public void generateSessions(int amountOfSessions) {
        for (int i = 0; i < amountOfSessions; i++) {
            MovieEntity movieOfTheSession = movies.get(random.nextInt(0, movies.size()));
            sessions.add(new MovieSessionEntity(movieOfTheSession));
        }
    }

    /**
     * Generate a seating plan for the cinema.
     * All seats have a seatNumber and a rowNumber.
     * */
    private void generateSeats() {
        for (int i = 0; i < TOTAL_SEATS_COUNT; i++) {
            int columnNumber = (i % SEAT_COLUMNS_COUNT) + 1; // columnNumber indexing starts at 1
            int distanceFromCenterColumn = Math.abs(CENTRE_SEAT_COLUMN_NUMBER - columnNumber);

            int rowNumber = (i / SEAT_COLUMNS_COUNT) + 1;  // rowNumber indexing starts at 1
            int distanceFromCenterRow = Math.abs(CENTRE_SEAT_ROW_NUMBER - rowNumber);

            int distanceFromCenter = distanceFromCenterColumn + distanceFromCenterRow;
            seats.add(new SeatEntity(rowNumber, i + 1, distanceFromCenter));  // seatNumber indexing starts at 1
        }
    }

    /**
     * Generate a random occupation status for all seats.
     * Then find and select the best available set of neighbouring seats.
     * Neighbouring eats must be next to each other and in the same row.
     * @param ticketCount amount of seats to be selected
     * @return list containing json data about all seats
     * */
    public Optional<List<Map<String, String>>> getNeighbouringSeats(int ticketCount) {
        // generate random occupation status for each seat
        seats.forEach(SeatEntity::setRandomOccupationStatus);

        // find all available (free) seat combinations
        HashMap<List<SeatEntity>, Integer> availableSeatingOptions = new HashMap<>();

        for (SeatEntity seat : seats.subList(0, seats.size() + 1 - ticketCount)) {
            int seatIndex = seat.getSeatNumber() - 1;
            // create list of neighbouring seats
            // size matches the amount of tickets bought
            List<SeatEntity> neighbouringSeats = seats.subList(seatIndex, seatIndex + ticketCount);
            boolean allSeatsAreFree = neighbouringSeats.stream()
                    .allMatch(SeatEntity::isFree);
            boolean allSeatsAreInSameRow = neighbouringSeats.stream()
                    .mapToInt(SeatEntity::getRowNumber)
                    .distinct()
                    .toArray().length == 1;
            // check whether all neighbouring seats are free and in the same row
            if (allSeatsAreFree && allSeatsAreInSameRow) {
                int totalDistanceFromCenter = neighbouringSeats.stream()
                        .mapToInt(SeatEntity::getDistanceFromCenter)
                        .sum();
                availableSeatingOptions.put(neighbouringSeats, totalDistanceFromCenter);
            }
        }

        // find the best available seating option from hashmap
        Optional<List<SeatEntity>> bestAvailableSeats = availableSeatingOptions.keySet().stream()
                .min(Comparator.comparing(availableSeatingOptions::get));  // compare by distanceFromCenter

        if (bestAvailableSeats.isPresent()) {
            // marks seats as selected
            bestAvailableSeats.get().forEach(x -> x.setOccupationStatus(SELECTED));
            return Optional.of(seats.stream()
                            .map(SeatEntity::toJson)
                            .toList()
            );
        }
        return Optional.empty();
    }

    /**
     * Select the best available seats, even when they are not neighbouring.
     * TODO: Try to get smaller amount of neighbouring seats instead of any (closest to center) seats.
     * @param ticketCount amount of seats to be selected
     * @return list containing json data about all seats
     * */
    public Optional<List<Map<String, String>>> getAnySeats(int ticketCount) {
        List<SeatEntity> result = seats.stream()
                .filter(SeatEntity::isFree)
                .sorted(Comparator.comparing(SeatEntity::getDistanceFromCenter))
                .toList();
        if (result.size() >= ticketCount) {
            result.subList(0, ticketCount).forEach(x -> x.setOccupationStatus(SELECTED));  // select best seats
            return Optional.of(seats.stream()
                    .map(SeatEntity::toJson)
                    .toList());
        }
        return Optional.empty();
    }

    /**
     * Filter available movies by genre, age rating, starting time and language.
     * TODO: Make front-end dropdown menus (that contain filtering options) dynamic.
     * @param genre required genre
     * @param ageRating required age rating
     * @param startingHour minimum starting time
     * @param language required language
     * @return List containing filtered movies.
     * */
    public List<MovieSessionEntity> getFilteredSessions(String genre, String ageRating, String startingHour, String language) {
        List<MovieSessionEntity> result = new ArrayList<>(sessions);
        // value "-" means that the specific category must not be filtered
        if (!"-".equals(genre)) {  // genre
            result = result.stream()
                    .filter(x -> x.getGenre().equals(genre))
                    .toList();
        }
        if (!"-".equals(ageRating)) {  // age rating
            result = result.stream()
                    .filter(x -> x.getAgeRating().equals(ageRating))
                    .toList();
        }
        if (!"-".equals(startingHour)) {  // starting time (hour)
            int hour = Integer.parseInt(startingHour);
            result = result.stream()
                    .filter(x -> x.getSessionStartTime().getHour() > hour)
                    .toList();
        }
        if (!"-".equals(language)) {  // language
            result = result.stream()
                    .filter(x -> x.getLanguage().equals(language))
                    .toList();
        }
        return result.stream()
                .sorted(Comparator.comparing(MovieSessionEntity::getSessionStartTime))
                .toList();
    }

    /**
     * Get session by its id if the session present.
     * @param id id of the movie
     * @return Optional object possibly containing a movie
     * */
    public Optional<MovieSessionEntity> getSessionById(int id) {
        return sessions.stream().filter(x -> x.getId() == id).findFirst();
    }

    /**
     * Get sessions sorted by session starting time.
     * */
    public List<MovieSessionEntity> getSessions() {
        return sessions.stream()
                .sorted(Comparator.comparing(MovieSessionEntity::getSessionStartTime))
                .toList();
    }
}
