package com.cgi.cinema.cinema;

import com.cgi.cinema.movie.Movie;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.cgi.cinema.cinema.Seat.OccupationStatus.SELECTED;


public class Cinema {

    private static final int MOVIE_COUNT = 30;

    // changing the number of seats would currently break front-end
    private static final int SEAT_ROWS_COUNT = 9;  // should be an odd number
    private static final int SEAT_COLUMNS_COUNT = 15;  // should be an odd number
    private static final int CENTRE_ROW_NUMBER = (SEAT_ROWS_COUNT / 2) + 1;  // indexing starts at 1
    private static final int CENTRE_COLUMN_NUMBER = (SEAT_COLUMNS_COUNT / 2) + 1;  // indexing starts at 1
    private static final int SEAT_COUNT = SEAT_COLUMNS_COUNT * SEAT_ROWS_COUNT;

    private final List<String> genres = Movie.genres;
    private final List<String> ageRatings = Movie.ageRatings;
    private final List<Integer> sessionStartMinutes = Movie.sessionStartMinutes;
    private final List<String> languages = Movie.languages;

    private final List<Movie> movies = new ArrayList<>();
    private final List<Seat> seats = new ArrayList<>();

    public Cinema() {
        generateMovies(MOVIE_COUNT);
        generateSeats();
    }

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
     * Generate and save new movies with randomized parameters.
     * @param amountOfMovies amount of movies to be created
     * */
    public void generateMovies(int amountOfMovies) {
        Random random = new Random();
        for (int i = 0; i < amountOfMovies; i++) {
            movies.add(new Movie(
                    "Movie" + i,
                    genres.get(random.nextInt(0, genres.size())),
                    ageRatings.get(random.nextInt(0, ageRatings.size())),
                    LocalTime.of(
                            random.nextInt(8, 23),  // hours
                            sessionStartMinutes.get(random.nextInt(0, sessionStartMinutes.size()))),  // minutes
                    languages.get(random.nextInt(0, languages.size())))
            );
        }
    }

    public List<Movie> getMovies() {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getSessionStartTime))
                .toList();
    }

    /**
     * Get movie by its id if the movie present.
     * @param id id of the movie
     * @return Optional object possibly containing a movie
     * */
    public Optional<Movie> getMovieById(int id) {
        return movies.stream().filter(x -> x.getId() == id).findFirst();
    }

    public Optional<List<Map<String, String>>> getRandomizedSeatingPlan(int ticketCount) {
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

    public List<Movie> getFilteredMovies(String genre, String ageRating, String startingHour, String language) {
        List<Movie> result = new ArrayList<>(movies);
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
        return result;
    }
}
