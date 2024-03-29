package com.cgi.cinema.cinema;

import com.cgi.cinema.session.Movie;
import com.cgi.cinema.session.Session;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.cgi.cinema.cinema.Seat.OccupationStatus.SELECTED;


public class Cinema {

    private static final int SESSION_COUNT = 35;

    // changing the number of seats would currently break front-end
    private static final int SEAT_ROWS_COUNT = 9;  // should be an odd number
    private static final int SEAT_COLUMNS_COUNT = 15;  // should be an odd number
    private static final int CENTRE_ROW_NUMBER = (SEAT_ROWS_COUNT / 2) + 1;  // indexing starts at 1
    private static final int CENTRE_COLUMN_NUMBER = (SEAT_COLUMNS_COUNT / 2) + 1;  // indexing starts at 1
    private static final int SEAT_COUNT = SEAT_COLUMNS_COUNT * SEAT_ROWS_COUNT;

    private final List<Movie> movies = new ArrayList<>();
    private final List<Session> sessions = new ArrayList<>();
    private final List<Seat> seats = new ArrayList<>();

    /**
     * Initialize Cinema class by generating movies and a seating plan.
     * */
    public Cinema() {
        generateMovies();
        generateSessions(SESSION_COUNT);
        generateSeats();
    }

    /**
     * Generate and save new movies with randomized parameters.
     * */
    public void generateMovies() {
        for (String movieTitle : Movie.movieTitles) {
            movies.add(new Movie(movieTitle));
        }
    }

    /**
     * Generate and save new sessions with randomized parameters.
     * @param amountOfSessions amount of sessions to be created
     * */
    public void generateSessions(int amountOfSessions) {
        Random random = new Random();
        for (int i = 0; i < amountOfSessions; i++) {
            Movie movieOfTheSession = movies.get(random.nextInt(0, movies.size()));
            sessions.add(new Session(movieOfTheSession));
        }
    }

    /**
     * Generate a seating plan for the cinema.
     * All seats have a seatNumber and a rowNumber.
     * */
    private void generateSeats() {
        for (int i = 0; i < SEAT_COUNT; i++) {
            int columnNumber = (i % SEAT_COLUMNS_COUNT) + 1; // columnNumber indexing starts at 1
            int distanceFromCenterColumn = Math.abs(CENTRE_COLUMN_NUMBER - columnNumber);

            int rowNumber = (i / SEAT_COLUMNS_COUNT) + 1;  // rowNumber indexing starts at 1
            int distanceFromCenterRow = Math.abs(CENTRE_ROW_NUMBER - rowNumber);

            int distanceFromCenter = distanceFromCenterColumn + distanceFromCenterRow;
            seats.add(new Seat(rowNumber, i + 1, distanceFromCenter));  // seatNumber indexing starts at 1
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
        seats.forEach(Seat::setRandomOccupationStatus);

        // find all available (free) seat combinations
        HashMap<List<Seat>, Integer> availableSeatingOptions = new HashMap<>();

        for (Seat seat : seats.subList(0, seats.size() + 1 - ticketCount)) {
            int seatIndex = seat.getSeatNumber() - 1;
            // create list of neighbouring seats
            // size matches the amount of tickets bought
            List<Seat> neighbouringSeats = seats.subList(seatIndex, seatIndex + ticketCount);
            boolean allSeatsAreFree = neighbouringSeats.stream()
                    .allMatch(Seat::isFree);
            boolean allSeatsAreInSameRow = neighbouringSeats.stream()
                    .mapToInt(Seat::getRowNumber)
                    .distinct()
                    .toArray().length == 1;
            // check whether all neighbouring seats are free and in the same row
            if (allSeatsAreFree && allSeatsAreInSameRow) {
                int totalDistanceFromCenter = neighbouringSeats.stream()
                        .mapToInt(Seat::getDistanceFromCenter)
                        .sum();
                availableSeatingOptions.put(neighbouringSeats, totalDistanceFromCenter);
            }
        }

        // find the best available seating option from hashmap
        Optional<List<Seat>> bestAvailableSeats = availableSeatingOptions.keySet().stream()
                .sorted(Comparator.comparing(availableSeatingOptions::get))  // compare by distanceFromCenter
                .findFirst();

        if (bestAvailableSeats.isPresent()) {
            // marks seats as selected
            bestAvailableSeats.get().forEach(x -> x.setOccupationStatus(SELECTED));
            return Optional.of(seats.stream()
                            .map(Seat::toJson)
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
        List<Seat> result = seats.stream()
                .filter(Seat::isFree)
                .sorted(Comparator.comparing(Seat::getDistanceFromCenter))
                .toList();
        if (result.size() >= ticketCount) {
            result.subList(0, ticketCount).forEach(x -> x.setOccupationStatus(SELECTED));  // select best seats
            return Optional.of(seats.stream()
                    .map(Seat::toJson)
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
    public List<Session> getFilteredSessions(String genre, String ageRating, String startingHour, String language) {
        List<Session> result = new ArrayList<>(sessions);
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
                .sorted(Comparator.comparing(Session::getSessionStartTime))
                .toList();
    }

    /**
     * Get session by its id if the session present.
     * @param id id of the movie
     * @return Optional object possibly containing a movie
     * */
    public Optional<Session> getSessionById(int id) {
        return sessions.stream().filter(x -> x.getId() == id).findFirst();
    }

    /**
     * Get sessions sorted by session starting time.
     * */
    public List<Session> getSessions() {
        return sessions.stream()
                .sorted(Comparator.comparing(Session::getSessionStartTime))
                .toList();
    }
}
