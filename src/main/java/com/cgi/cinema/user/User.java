package com.cgi.cinema.user;

import com.cgi.cinema.movie.Movie;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final List<Movie> watchingHistory = new ArrayList<>();

    /**
     * Add a new movie to User's watching history.
     * @param movie new movie
     * @return boolean whether the addition was successful
     * */
    public boolean addToWatchingHistory(Movie movie) {
        watchingHistory.add(movie);
        return true;
    }

}
