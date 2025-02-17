package com.cgi.app.entity.movie;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Random;

import static com.cgi.app.util.Constants.MOVIE_SESSION_LANGUAGES;
import static com.cgi.app.util.Constants.SESSION_STARTING_TIME_MINUTES;

@Getter
@Setter
public class MovieSessionEntity {

    private final MovieEntity movie;
    private final LocalTime sessionStartTime;
    private final String sessionLanguage;
    private final int sessionId;
    private static int nextSessionId = 1;

    /**
     * Initialize Session with a movie and random session parameters.
     * TODO: Add a date to each session.
     *
     * @param movie movie of the session
     */
    public MovieSessionEntity(MovieEntity movie) {
        Random random = new Random();

        this.movie = movie;
        this.sessionStartTime = LocalTime.of(
                random.nextInt(8, 23),  // hours
                SESSION_STARTING_TIME_MINUTES.get(random.nextInt(0, SESSION_STARTING_TIME_MINUTES.size())));  // minutes
        this.sessionLanguage = MOVIE_SESSION_LANGUAGES.get(random.nextInt(0, MOVIE_SESSION_LANGUAGES.size()));

        this.sessionId = this.getNextSessionId();
    }

    /**
     * Calculate id for the next session.
     */
    private int getNextSessionId() {
        return nextSessionId++;
    }

}
