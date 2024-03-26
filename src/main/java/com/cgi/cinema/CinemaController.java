package com.cgi.cinema;

import com.cgi.cinema.cinema.Cinema;
import com.cgi.cinema.movie.Movie;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CinemaController {

    private Cinema cinema = new Cinema();

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

}
