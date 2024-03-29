package com.cgi.cinema.session;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Session {

    // back-end can handle modifying these lists, but front-end has static dropdown menus
    public static final List<Integer> sessionStartMinutes = List.of(0, 15, 30, 45);
    public static final List<String> languages = List.of("Estonian", "English", "Russian");

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
                sessionStartMinutes.get(random.nextInt(0, sessionStartMinutes.size())));  // minutes
        this.language = languages.get(random.nextInt(0, languages.size()));
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
    }public String getMovieTitle() {
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
