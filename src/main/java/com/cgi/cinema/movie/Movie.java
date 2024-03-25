package com.cgi.cinema.movie;

import java.time.LocalTime;

public class Movie {

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
        return title + "    " + genre + "    " + ageRating + "    " + sessionStartTime.toString() + "  " + language;
    }
}
