package com.cgi.cinema;

import com.cgi.cinema.movie.Movie;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CinemaController {

    @RequestMapping("/movies")
    public Map<String, String> getMovies() {
        Map<String, String> response = new HashMap<>();
        Movie movie = new Movie("Movie 1", "action", "PG-13", LocalTime.of(21, 0), "english");
        response.put("title", movie.getTitle());
        response.put("movieAsString", movie.toString());
        return response;
    }

}
