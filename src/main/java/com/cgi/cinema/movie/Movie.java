package com.cgi.cinema.movie;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie {

    // back-end can handle modifying these lists, but front-end has static dropdown menus
    public static final List<String> genres = List.of("Comedy", "Action", "Horror", "Nature");
    public static final List<String> ageRatings = List.of("G", "PG", "PG-13", "NC-17");
    public static final List<Integer> sessionStartMinutes = List.of(0, 15, 30, 45);
    public static final List<String> languages = List.of("Estonian", "English", "Russian");

    private final String title;
    private final String genre;
    private final String ageRating;
    private final LocalTime sessionStartTime;
    private final String language;
    private final int id;
    private static int nextId = 1;

    /**
     * Initialize Movie class.
     * @param title movie title
     * @param genre movie genre
     * @param ageRating age rating of the movie
     * @param sessionStart LocalTime representing when the session begins
     * @param language movie language
     * */
    public Movie(String title, String genre, String ageRating, LocalTime sessionStart, String language) {
        this.title = title;
        this.genre = genre;
        this.ageRating = ageRating;
        this.sessionStartTime = sessionStart;
        this.language = language;
        this.id = this.getNextId();
    }

    /**
     * Calculate id for the next movie.
     * */
    private int getNextId() {
        return nextId++;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
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
        return String.format("%-15s", title)
                + String.format("%-15s", genre)
                + String.format("%-15s", ageRating)
                + String.format("%-15s", sessionStartTime.toString())
                + String.format("%-10s", language);
    }

    public Map<String, String> toJson() {
        Map<String, String> movieHashMap = new HashMap<>();
        movieHashMap.put("id", Integer.toString(id));
        movieHashMap.put("asString", this.toString());
        return movieHashMap;
    }
}
