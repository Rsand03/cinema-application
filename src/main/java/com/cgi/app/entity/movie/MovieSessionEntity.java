package com.cgi.app.entity.movie;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Random;

import static com.cgi.app.util.Constants.MOVIE_SESSION_LANGUAGES;
import static com.cgi.app.util.Constants.MOVIE_SESSION_STARTING_TIME_HOURS;
import static com.cgi.app.util.Constants.MOVIE_SESSION_STARTING_TIME_MINUTES;

@Getter
@Setter
public class MovieSessionEntity {

    // Mocked entity without DB
    private final MovieEntity movie;
    private final LocalTime sessionStartTime;
    private final String sessionLanguage;
    private final int sessionId;

    /**
     * Initialize Session with a movie and random session parameters.
     * TODO: Add a date to each session.
     *
     * @param movie movie of the session
     */
    public MovieSessionEntity(MovieEntity movie, Integer sessionId) {
        Random random = new Random();

        this.movie = movie;
        this.sessionStartTime = LocalTime.of(
                MOVIE_SESSION_STARTING_TIME_HOURS.get(random.nextInt(MOVIE_SESSION_STARTING_TIME_HOURS.size())),  // hours
                MOVIE_SESSION_STARTING_TIME_MINUTES.get(random.nextInt(MOVIE_SESSION_STARTING_TIME_MINUTES.size())));  // minutes
        this.sessionLanguage = MOVIE_SESSION_LANGUAGES.get(random.nextInt(0, MOVIE_SESSION_LANGUAGES.size()));

        this.sessionId = sessionId;
    }

}
