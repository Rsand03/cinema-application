package com.cgi.app.controller;

import com.cgi.app.dto.MovieSessionAttributesDto;
import com.cgi.app.dto.MovieSessionDto;
import com.cgi.app.dto.MovieSessionFilteringDto;
import com.cgi.app.entity.movie.MovieSessionEntity;
import com.cgi.app.service.MovieSessionService;
import com.cgi.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/movies")
@RequiredArgsConstructor
public class MovieSessionController {

    private final MovieSessionService movieSessionService;
    private final UserService userService;


    /**
     * Get all movie attributes such as available age ratings, languages etc.
     *
     * @return A {@link MovieSessionAttributesDto} containing distinct genres, age ratings,
     * session starting times, and languages.
     */
    @GetMapping("/attributes")
    public ResponseEntity<MovieSessionAttributesDto> getMovieSessionAttributes() {
        return ResponseEntity.ok(movieSessionService.getMovieSessionAttributes());
    }

    /**
     * Get data about all available movie sessions.
     *
     * @return data in json format, HashMap for each movie session:
     * "id": movie session id
     * "asString": all necessary movie data as a formatted string
     */
    @GetMapping("")
    public ResponseEntity<List<MovieSessionDto>> getAvailableMovieSessions() {
        return ResponseEntity.ok(movieSessionService.getAvailableMovieSessions());
    }

    /**
     * Get data about movie sessions that match the filtering criteria.
     * The filtering criteria (genre, age rating, session start time, and language)
     * are passed in the {@link MovieSessionFilteringDto}.
     *
     * @param filteringParams contains the filtering criteria for movie sessions.
     * @return a list of filtered movie sessions in JSON format.
     */
    @GetMapping("filtered")
    public ResponseEntity<List<MovieSessionDto>> getFilteredMovies(@ModelAttribute MovieSessionFilteringDto filteringParams) {
        return ResponseEntity.ok(
                movieSessionService.getFilteredMovieSessions(filteringParams)
        );
    }

    /**
     * Get data about top 5 recommended movies based on user's watching history.
     *
     * @return data in json format, HashMap for each movie session:
     * "id": movie session id
     * "asString": all necessary movie data as a formatted string
     */
    @GetMapping("/recommended")
    public ResponseEntity<List<MovieSessionDto>> getRecommendedMovies() {
        List<MovieSessionDto> result = userService.getTopFiveRecommendedMovies();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Verify selected movie session and add it to user's watching history in order to recommend movies in the future.
     *
     * @return Http status 200: verification was successful
     */
    @PatchMapping("/selection")
    public ResponseEntity<HttpStatus> applyMovieSessionSelection(@RequestParam Integer id) {
        Optional<MovieSessionEntity> selectedMovieSession = movieSessionService.getMovieSessionById(id);
        if (selectedMovieSession.isPresent()) {
            userService.addToWatchingHistory(selectedMovieSession.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
