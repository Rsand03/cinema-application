package com.cgi.cinema.user;

import com.cgi.cinema.movie.Movie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class User {

    private final HashMap<String, Integer> genresWatched = new HashMap<>();
    private final List<Movie> moviesWatched = new ArrayList<>();

    /**
     * Initialize User class.
     * Create a HashMap for logging the genres of all watched movies,
     * which is later used for movie recommendation algorithm.
     * */
    public User() {
        for (String genre : Movie.genres) {
            genresWatched.put(genre, 0);
        }
    }

    /**
     * Update User's watched genres HashMap and log watched movie.
     * @param movie new movie
     * */
    public void addToWatchingHistory(Movie movie) {
        String genre = movie.getGenre();
        genresWatched.put(genre, genresWatched.get(genre) + 1);
        moviesWatched.add(movie);
    }

    /**
     * Determine how many times the genre of the movie has been watched.
     * @param movie movie to be checked
     * @return time genre watched
     * */
    private Integer getTimesGenreWatched(Movie movie) {
        return genresWatched.get(movie.getGenre());
    }

    /**
     * Recommend new movies based on user's watching history and watched genres.
     * @param movies list of movies to recommend from
     * @return top 5 most fitting movies
     * */
    public List<Movie> getTopFiveRecommendedMovies(List<Movie> movies) {
        if (moviesWatched.isEmpty()) {
            return List.of();
        }
        List<Movie> result = movies.stream()
                .filter(x -> !moviesWatched.contains(x))  // filter out already watched movies
                .sorted(Comparator
                        .comparing(this::getTimesGenreWatched))
                .toList()
                .reversed();
        if (result.size() <= 5) {
            return result;
        }
        return result.subList(0, 6);
    }

}
