package com.cgi.app.entity.user;

import com.cgi.app.entity.movie.MovieSessionEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.cgi.app.util.Constants.MOVIE_GENRES;

@Getter
public class UserEntity {

    // Mocked entity without DB
    private final HashMap<String, Integer> genresWatched = new HashMap<>();
    private final List<String> alreadyWatchedMovies = new ArrayList<>();

    /**
     * Initialize User class.
     * Create a HashMap for logging the genres of all watched movies,
     * which is later used for movie session recommendation algorithm.
     */
    public UserEntity() {
        for (String genre : MOVIE_GENRES) {
            genresWatched.put(genre, 0);
        }
    }

    /**
     * Update User's watched genres HashMap and log watched movie title.
     *
     * @param movieSession new watched movie
     */
    public void addToWatchingHistory(MovieSessionEntity movieSession) {
        String genre = movieSession.getMovie().getGenre();
        genresWatched.put(genre, genresWatched.get(genre) + 1);
        alreadyWatchedMovies.add(movieSession.getMovie().getTitle());
    }

}
