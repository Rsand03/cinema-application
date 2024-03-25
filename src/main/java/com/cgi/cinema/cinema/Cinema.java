package com.cgi.cinema.cinema;

import com.cgi.cinema.movie.Movie;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
                    genres.get(random.nextInt(0, 3)),
                    ageRatings.get(random.nextInt(0, 3)),
                    LocalTime.of(
                            random.nextInt(8, 23),
                            sessionStartMinutes.get(random.nextInt(0, 3))),
                    languages.get(random.nextInt(0, 3)))
            );
        }
    }

    public List<Movie> getMovies() {
        return new ArrayList<>(movies);
    }

    public List<Movie> getFilteredMovies(String genre, String ageRating, Integer hour, String language) {
        List<Movie> result = new ArrayList<>(movies);
        if (genre != null) {
            result = result.stream().filter(x -> x.getGenre().equals(genre)).toList();
        }
        if (ageRating != null) {
            result = result.stream().filter(x -> x.getAgeRating().equals(ageRating)).toList();
        }
        if (hour != null) {
            result = result.stream().filter(x -> x.getSessionStartTime().getHour() > hour).toList();
        }
        if (hour != null) {
            result = result.stream().filter(x -> x.getLanguage().equals(language)).toList();
        }
        return result;
    }

}
