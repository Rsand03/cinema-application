package com.cgi.app.entity.movie;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

import static com.cgi.app.util.Constants.MOVIE_AGE_RATINGS;
import static com.cgi.app.util.Constants.MOVIE_GENRES;

@Getter
@Setter
public class MovieEntity {

    // Mocked entity without DB
    private final String title;
    private final String genre;
    private final String ageRating;

    /**
     * Initialize MovieEntity class with randomized parameters.
     * The Movie class is used just as an input for Session class, which
     * links each movie title to specific age rating and genre.
     */
    public MovieEntity(String title) {
        Random random = new Random();
        this.title = title;
        this.genre = MOVIE_GENRES.get(random.nextInt(0, MOVIE_GENRES.size()));
        this.ageRating = MOVIE_AGE_RATINGS.get(random.nextInt(0, MOVIE_AGE_RATINGS.size()));
    }

}
