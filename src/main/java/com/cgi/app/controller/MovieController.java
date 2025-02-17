package com.cgi.app.controller;

import com.cgi.app.dto.MovieSessionDto;
import com.cgi.app.entity.movie.MovieSessionEntity;
import com.cgi.app.service.MovieSessionService;
import com.cgi.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieSessionService movieSessionService;
    private final UserService userService;

    /**
     * Get data about all available movie sessions.
     *
     * @return data in json format, HashMap for each movie session:
     * "id": movie session id
     * "asString": all necessary movie data as a formatted string
     */
    @RequestMapping("/movies")
    public ResponseEntity<List<MovieSessionDto>> getAvailableMovieSessions() {
        return ResponseEntity.ok(movieSessionService.getAvailableMovieSessions());
    }

    /**
     * Get data about all movie sessions that match the specified criteria.
     *
     * @param genre            required movie genre
     * @param ageRating        required movie age rating
     * @param sessionStartTime earliest acceptable session starting time
     * @param language         required language of the session
     * @return data in json format, HashMap for each movie session:
     * "id": movie session id
     * "asString": all necessary movie data as a formatted string
     */
    @RequestMapping("/movies/filtered")
    public ResponseEntity<List<MovieSessionDto>> getFilteredMovies(@RequestParam String genre,
                                                                   @RequestParam String ageRating,
                                                                   @RequestParam String sessionStartTime,
                                                                   @RequestParam String language) {
        return ResponseEntity.ok(
                movieSessionService.getFilteredMovieSessions(genre, ageRating, sessionStartTime, language)
        );
    }

    /**
     * Get data about top 5 recommended movies based on user's watching history.
     *
     * @return data in json format, HashMap for each movie session:
     * "id": movie session id
     * "asString": all necessary movie data as a formatted string
     */
    @RequestMapping("/movies/recommended")
    public ResponseEntity<List<MovieSessionDto>> getRecommendedMovies() {
        List<MovieSessionDto> result = userService.getTopFiveRecommendedMovies();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Verify selected movie session and add it to user's watching history.
     *
     * @return Http status 200: verification was successful
     */
    @RequestMapping("/movies/selection")
    public ResponseEntity<HttpStatus> applyMovieSessionSelection(@RequestParam Integer id) {
        Optional<MovieSessionEntity> selectedMovieSession = movieSessionService.getMovieSessionById(id);
        if (selectedMovieSession.isPresent()) {
            userService.addToWatchingHistory(selectedMovieSession.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
