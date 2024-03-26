package com.cgi.cinema.cinema;

import com.cgi.cinema.movie.Movie;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Cinema {

    private final List<String> genres = List.of("Comedy", "Action", "Horror", "Nature");
    private final List<String> ageRatings = List.of("G", "PG", "PG-13", "NC-17");
    private final List<Integer> sessionStartMinutes = List.of(0, 15, 30, 45);
    private final List<String> languages = List.of("Estonian", "English", "Russian");

    private final List<Movie> movies = new ArrayList<>();

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
                            random.nextInt(8, 23),
                            sessionStartMinutes.get(random.nextInt(0, sessionStartMinutes.size()))),
                    languages.get(random.nextInt(0, languages.size())))
            );
        }
    }

    public List<Movie> getMovies() {
        return new ArrayList<>(movies);
    }

    public List<Movie> getFilteredMovies(String genre, String ageRating, String startingHour, String language) {
        List<Movie> result = new ArrayList<>(movies);
        // value "-" means that the specific category must not be filtered
        if (!"-".equals(genre)) {
            result = result.stream().filter(x -> x.getGenre().equals(genre)).toList();
        }
        if (!"-".equals(ageRating)) {
            result = result.stream().filter(x -> x.getAgeRating().equals(ageRating)).toList();
        }
        if (!"-".equals(startingHour)) {
            int hour = Integer.parseInt(startingHour);
            result = result.stream().filter(x -> x.getSessionStartTime().getHour() > hour).toList();
        }
        if (!"-".equals(language)) {
            result = result.stream().filter(x -> x.getLanguage().equals(language)).toList();
        }
        return result;
    }

    /**
     * Get movie by its id if the movie present.
     * @param id id of the movie
     * @return Optional object possibly containing a movie
     * */
    public Optional<Movie> getMovieById(int id) {
        return movies.stream().filter(x -> x.getId() == id).findFirst();
    }

}
