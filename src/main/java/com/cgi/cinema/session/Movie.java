package com.cgi.cinema.session;

import java.util.List;
import java.util.Random;


public class Movie {

    // back-end can handle modifying these lists, but front-end has static dropdown menus
    public static final List<String> movieTitles = List.of(
            "Barbie", "Oppenheimer", "Avengers", "The Ivory Game", "The Amazon",
            "Dont look up", "Scream", "Saw 1", "Saw 2", "Saw 3", "Saw 4", "Saw 5", "Saw 6"
    );
    public static final List<String> genres = List.of("Comedy", "Action", "Horror", "Nature");
    public static final List<String> ageRatings = List.of("G", "PG", "PG-13", "NC-17");

    private final String title;
    private final String genre;
    private final String ageRating;

    /**
     * Initialize Movie class with randomized parameters.
     * The Movie class is used just as an input for Session class, which
     * links each movie title with specific age rating and genre.
     * */
    public Movie(String title) {
        Random random = new Random();
        this.title = title;
        this.genre = genres.get(random.nextInt(0, genres.size()));
        this.ageRating = ageRatings.get(random.nextInt(0, ageRatings.size()));
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
