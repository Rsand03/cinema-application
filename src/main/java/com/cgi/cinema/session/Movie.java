package com.cgi.cinema.session;

import java.util.Random;

import static com.cgi.cinema.Constants.MOVIE_AGE_RATINGS;
import static com.cgi.cinema.Constants.MOVIE_GENRES;


public class Movie {

    private final String title;
    private final String genre;
    private final String ageRating;

    /**
     * Initialize Movie class with randomized parameters.
     * The Movie class is used just as an input for Session class, which
     * links each movie title to specific age rating and genre.
     * */
    public Movie(String title) {
        Random random = new Random();
        this.title = title;
        this.genre = MOVIE_GENRES.get(random.nextInt(0, MOVIE_GENRES.size()));
        this.ageRating = MOVIE_AGE_RATINGS.get(random.nextInt(0, MOVIE_AGE_RATINGS.size()));
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
}
