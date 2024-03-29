package com.cgi.cinema;

import com.cgi.cinema.cinema.Cinema;
import com.cgi.cinema.session.Session;
import com.cgi.cinema.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CinemaController {

    private final Cinema cinema = new Cinema();
    private final User user = new User();

    @RequestMapping("/movies")
    public List<Map<String, String>> getMovies() {
        return cinema.getSessions().stream()
                .map(Session::toJson)
                .toList();
    }

    @RequestMapping("/movies/filtered")
    public List<Map<String, String>> getFilteredMovies(@RequestParam String genre,
                                                       @RequestParam String ageRating,
                                                       @RequestParam String sessionStartTime,
                                                       @RequestParam String language) {
        return cinema.getFilteredSessions(genre, ageRating, sessionStartTime, language)
                .stream()
                .map(Session::toJson)
                .toList();
    }

    @RequestMapping("/movies/recommended")
    public ResponseEntity<List<Map<String, String>>> getFilteredMovies() {
        List<Map<String, String>> result = user.getTopFiveRecommendedMovies(cinema.getSessions()).stream()
                .map(Session::toJson)
                .toList();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping("/movies/selection")
    public ResponseEntity<HttpStatus> verifyMovieSessionSelection(@RequestParam Integer id) {
        Optional<Session> selectedMovieSession = cinema.getSessionById(id);
        if (selectedMovieSession.isPresent()) {
            user.addToWatchingHistory(selectedMovieSession.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping("/seats")
    public ResponseEntity<List<Map<String, String>>> getRandomSeatingPlan(@RequestParam Integer ticketCount) {
        Optional<List<Map<String, String>>> neighbouringSeats = cinema.getNeighbouringSeats(ticketCount);
        if (neighbouringSeats.isPresent()) {
            return new ResponseEntity<>(neighbouringSeats.get(), HttpStatus.OK);
        }

        Optional<List<Map<String, String>>> anySeats = cinema.getAnySeats(ticketCount);
        if (anySeats.isPresent()) {
            return new ResponseEntity<>(anySeats.get(), HttpStatus.PARTIAL_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
