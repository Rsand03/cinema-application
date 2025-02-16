package com.cgi.cinema.session;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.cgi.cinema.Constants.MOVIE_SESSION_LANGUAGES;
import static com.cgi.cinema.Constants.SESSION_STARTING_TIME_MINUTES;

public class Session {

    private final String movieTitle;
    private final String genre;
    private final String ageRating;
    private final LocalTime sessionStartTime;
    private final String language;
    private final int id;
    private static int nextId = 1;

    /**
     * Initialize Session with a movie and random session parameters.
     * TODO: Add a date to each session.
     * @param movie movie of the session
     * */
    public Session(Movie movie) {
        Random random = new Random();
        this.movieTitle = movie.getTitle();
        this.genre = movie.getGenre();
        this.ageRating = movie.getAgeRating();

        this.sessionStartTime = LocalTime.of(
                random.nextInt(8, 23),  // hours
                SESSION_STARTING_TIME_MINUTES.get(random.nextInt(0, SESSION_STARTING_TIME_MINUTES.size())));  // minutes
        this.language = MOVIE_SESSION_LANGUAGES.get(random.nextInt(0, MOVIE_SESSION_LANGUAGES.size()));
        this.id = this.getNextId();
    }

    /**
     * Calculate id for the next session.
     * */
    private int getNextId() {
        return nextId++;
    }

    public int getId() {
        return id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getGenre() {
        return genre;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public LocalTime getSessionStartTime() {
        return sessionStartTime;
    }

    public String getLanguage() {
        return language;
    }

    /**
     * Transform session data to a String that can be displayed in front-end.
     * @return formatted string
     * */
    @Override
    public String toString() {
        return String.format("%-17s", movieTitle)
                + String.format("%-15s", genre)
                + String.format("%-15s", ageRating)
                + String.format("%-15s", sessionStartTime.toString())
                + String.format("%-10s", language);
    }

    /**
     * Get session and movie data in json format.
     * @return session id and all info as a string
     * */
    public Map<String, String> toJson() {
        Map<String, String> movieHashMap = new HashMap<>();
        movieHashMap.put("id", Integer.toString(id));
        movieHashMap.put("asString", this.toString());
        return movieHashMap;
    }
}
