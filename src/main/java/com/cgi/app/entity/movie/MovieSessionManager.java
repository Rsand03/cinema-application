package com.cgi.app.entity.movie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.cgi.app.util.Constants.MOVIE_TITLES;
import static com.cgi.app.util.Constants.TOTAL_AVAILABLE_MOVIE_SESSIONS_COUNT;


public class MovieSessionManager {

    private final List<MovieEntity> movies = new ArrayList<>();
    private final List<MovieSessionEntity> sessions = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Initialize MovieSessionHandler class by generating movies and sessions.
     */
    public MovieSessionManager() {
        generateMovies();
        generateMovieSessions();
    }

    /**
     * Generate and save new movies with randomized parameters.
     */
    private void generateMovies() {
        for (String movieTitle : MOVIE_TITLES) {
            movies.add(new MovieEntity(movieTitle));
        }
    }

    /**
     * Generate and save new sessions with randomized parameters.
     */
    private void generateMovieSessions() {
        for (int i = 0; i < TOTAL_AVAILABLE_MOVIE_SESSIONS_COUNT; i++) {
            MovieEntity movieOfTheSession = movies.get(random.nextInt(0, movies.size()));
            sessions.add(new MovieSessionEntity(movieOfTheSession));
        }
    }

    /**
     * Get all movie sessions sorted by session starting time.
     */
    public List<MovieSessionEntity> getMovieSessions() {
        return sessions.stream()
                .sorted(Comparator.comparing(MovieSessionEntity::getSessionStartTime))
                .toList();
    }

}
