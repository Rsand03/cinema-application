package com.cgi.cinema;

import com.cgi.cinema.cinema.Cinema;
import com.cgi.cinema.movie.Movie;
import com.cgi.cinema.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CinemaController {

    private final Cinema cinema = new Cinema();
    private final User user = new User();

    @RequestMapping("/movies")
    public List<Map<String, String>> getMovies() {
        if (cinema.getMovies().isEmpty()) {
            cinema.generateMovies(30);
        }

        List<Map<String, String>> response = new ArrayList<>();
        for (Movie movie : cinema.getMovies()) {
            Map<String, String> movieHashMap = new HashMap<>();
            movieHashMap.put("id", Integer.toString(movie.getId()));
            movieHashMap.put("asString", movie.toString());
            response.add(movieHashMap);
        }
        return response;
    }

    @RequestMapping("/movies/filtered")
    public List<Map<String, String>> getFilteredMovies(@RequestParam String genre,
                                                       @RequestParam String ageRating,
                                                       @RequestParam String sessionStartTime,
                                                       @RequestParam String language) {
        List<Map<String, String>> response = new ArrayList<>();

        for (Movie movie : cinema.getFilteredMovies(genre, ageRating, sessionStartTime, language)) {
            Map<String, String> movieHashMap = new HashMap<>();
            movieHashMap.put("id", Integer.toString(movie.getId()));
            movieHashMap.put("asString", movie.toString());
            response.add(movieHashMap);
        }
        return response;
    }

    @RequestMapping("/movies/selection")
    public ResponseEntity<HttpStatus> verifyMovieSelection(@RequestParam Integer id) {
        Optional<Movie> newMovie = cinema.getMovieById(id);
        if (newMovie.isPresent()) {
            user.addToWatchingHistory(newMovie.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
