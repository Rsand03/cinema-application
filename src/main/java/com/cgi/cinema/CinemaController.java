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
        return cinema.getMovies().stream()
                .map(Movie::toJson)
                .toList();
    }

    @RequestMapping("/movies/filtered")
    public List<Map<String, String>> getFilteredMovies(@RequestParam String genre,
                                                       @RequestParam String ageRating,
                                                       @RequestParam String sessionStartTime,
                                                       @RequestParam String language) {
        return cinema.getFilteredMovies(genre, ageRating, sessionStartTime, language)
                .stream()
                .map(Movie::toJson)
                .toList();
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

    @RequestMapping("/seats")
    public ResponseEntity<List<Map<String, String>>> getRandomSeatingPlan(@RequestParam Integer ticketCount) {
        Optional<List<Map<String, String>>> seatingPlan = cinema.getRandomizedSeatingPlan(ticketCount);
        if (seatingPlan.isPresent()) {
            return new ResponseEntity<>(seatingPlan.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
