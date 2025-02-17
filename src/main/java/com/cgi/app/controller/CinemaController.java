package com.cgi.app.controller;

import com.cgi.app.entity.cinema.CinemaEntity;
import com.cgi.app.entity.movie.MovieSessionEntity;
import com.cgi.app.entity.user.UserEntity;
import com.cgi.app.service.CinemaService;
import com.cgi.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaEntity cinema = new CinemaEntity();
    private final UserEntity user = new UserEntity();

    private final CinemaService cinemaService;
    private final UserService userService;

    /**
     * Get data about all available movie sessions.
     *
     * @return data in json format, HashMap for each movie session:
     * "id": movie session id
     * "asString": all necessary movie data as a formatted string
     */
    @RequestMapping("/movies")
    public List<Map<String, String>> getAvailableMovieSessions() {
        return cinemaService.getAvailableMovieSessions();
    }

    /**
     * Get data about all movie sessions that match the specified criteria.
     * @param genre required movie genre
     * @param ageRating required movie age rating
     * @param sessionStartTime earliest acceptable session starting time
     * @param language required language of the session
     *
     * @return data in json format, HashMap for each movie session:
     * "id": movie session id
     * "asString": all necessary movie data as a formatted string
     */
    @RequestMapping("/movies/filtered")
    public List<Map<String, String>> getFilteredMovies(@RequestParam String genre,
                                                       @RequestParam String ageRating,
                                                       @RequestParam String sessionStartTime,
                                                       @RequestParam String language) {
        return cinemaService.getFilteredMovieSessions(genre, ageRating, sessionStartTime, language);
    }

    /**
     * Get data about top 5 recommended movies based on user's watching history.
     *
     * @return data in json format, HashMap for each movie session:
     * "id": movie session id
     * "asString": all necessary movie data as a formatted string
     */
    @RequestMapping("/movies/recommended")
    public ResponseEntity<List<Map<String, String>>> getFilteredMovies() {
        List<Map<String, String>> result = user.getTopFiveRecommendedMovies(cinema.getMovieSessions()).stream()
                .map(MovieSessionEntity::toJson)
                .toList();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Add selected movie session to user's watching history.
     *
     * @return Http status 200: verification was successful
     */
    @RequestMapping("/movies/selection")
    public ResponseEntity<HttpStatus> verifyMovieSessionSelection(@RequestParam Integer id) {
        Optional<MovieSessionEntity> selectedMovieSession = cinema.getSessionById(id);
        if (selectedMovieSession.isPresent()) {
            user.addToWatchingHistory(selectedMovieSession.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Get seating plan data.
     * Http status 200: found neighbouring seats
     * Http status 206: found seats, but not neighbouring
     *
     * @param ticketCount amount of seats to select
     *
     * @return data in json format, HashMap for each seat:
     * "seatNumber": seat number
     * "occupationStatus": state of the seat (FREE / SELECTED / OCCUPIED)
     */
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
