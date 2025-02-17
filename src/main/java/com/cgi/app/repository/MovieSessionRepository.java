package com.cgi.app.repository;

import com.cgi.app.entity.movie.MovieSessionManager;


public class MovieSessionRepository {

    private MovieSessionRepository() {
    }

    // Mocked repository functionality without DB
    public static final MovieSessionManager movieSessionManager = new MovieSessionManager();

}
