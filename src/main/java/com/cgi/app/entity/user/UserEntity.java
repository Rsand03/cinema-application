package com.cgi.app.entity.user;

import com.cgi.app.entity.movie.MovieSessionEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.cgi.app.util.Constants.MOVIE_GENRES;

public class UserEntity {

    private final HashMap<String, Integer> genresWatched = new HashMap<>();
    private final List<String> moviesWatched = new ArrayList<>();

    /**
     * Initialize User class.
     * Create a HashMap for logging the genres of all watched movies,
     * which is later used for movie session recommendation algorithm.
     * */
    public UserEntity() {
        for (String genre : MOVIE_GENRES) {
            genresWatched.put(genre, 0);
        }
    }

    /**
     * Update User's watched genres HashMap and log watched movie title.
     * @param movieSession new watched movie
     * */
    public void addToWatchingHistory(MovieSessionEntity movieSession) {
        String genre = movieSession.getGenre();
        genresWatched.put(genre, genresWatched.get(genre) + 1);
        moviesWatched.add(movieSession.getMovieTitle());
    }

    /**
     * Determine how many times the genre of the movie has been watched.
     * @param movieSession movie to be checked
     * @return time genre watched
     * */
    private Integer getTimesGenreWatched(MovieSessionEntity movieSession) {
        return genresWatched.get(movieSession.getGenre());
    }

    /**
     * Recommend new movie sessions based on user's watching history and watched genres.
     * @param movieSessions list of movie sessions to recommend from
     * @return top 5 most fitting movies
     * */
    public List<MovieSessionEntity> getTopFiveRecommendedMovies(List<MovieSessionEntity> movieSessions) {
        if (moviesWatched.isEmpty()) {
            return List.of();
        }
        List<MovieSessionEntity> result = movieSessions.stream()
                .filter(x -> !moviesWatched.contains(x.getMovieTitle()))  // filter out already watched movies
                .sorted(Comparator
                        .comparing(this::getTimesGenreWatched))
                .toList()
                .reversed();
        if (result.size() <= 5) {
            return result;
        }
        return result.subList(0, 6);
    }

}
