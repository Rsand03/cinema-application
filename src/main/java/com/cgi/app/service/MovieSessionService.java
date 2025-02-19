package com.cgi.app.service;

import com.cgi.app.dto.MovieSessionAttributesDto;
import com.cgi.app.dto.MovieSessionDto;
import com.cgi.app.dto.MovieSessionFilteringDto;
import com.cgi.app.dto.mapper.MovieSessionMapper;
import com.cgi.app.entity.movie.MovieSessionEntity;
import com.cgi.app.entity.movie.MovieSessionManager;
import com.cgi.app.repository.MovieSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.cgi.app.util.Constants.MOVIE_AGE_RATINGS;
import static com.cgi.app.util.Constants.MOVIE_GENRES;
import static com.cgi.app.util.Constants.MOVIE_SESSION_LANGUAGES;
import static com.cgi.app.util.Constants.MOVIE_SESSION_STARTING_TIME_HOURS;

@Service
@RequiredArgsConstructor
public class MovieSessionService {

    // Mocked repository functionality without DB
    private static final MovieSessionManager movieSessionManagerEntity = MovieSessionRepository.movieSessionManager;
    private final MovieSessionMapper movieSessionMapper;

    /**
     * Get all movie attributes such as available age ratings, languages etc.
     */
    public MovieSessionAttributesDto getMovieSessionAttributes() {
        MovieSessionAttributesDto dto = new MovieSessionAttributesDto();
        dto.setGenres(MOVIE_GENRES);
        dto.setAgeRatings(MOVIE_AGE_RATINGS);
        dto.setSessionStartingTimes(
                MOVIE_SESSION_STARTING_TIME_HOURS.stream()
                        .map(String::valueOf)
                        .toList()
        );
        dto.setLanguages(MOVIE_SESSION_LANGUAGES);
        return dto;
    }

    /**
     * Get data about all available movie sessions.
     */
    public List<MovieSessionDto> getAvailableMovieSessions() {
        return movieSessionMapper.toDtoList(movieSessionManagerEntity.getMovieSessions());
    }

    /**
     * Get movie session by its id if the session present.
     *
     * @param id id of the movie
     * @return Optional object possibly containing a movie
     */
    public Optional<MovieSessionEntity> getMovieSessionById(int id) {
        return movieSessionManagerEntity.getMovieSessions().stream()
                .filter(x -> x.getSessionId() == id)
                .findFirst();
    }

    /**
     * Filter available movie sessions by (optional) genre, age rating, starting time and language.
     * @return List containing filtered movies.
     */
    public List<MovieSessionDto> getFilteredMovieSessions(MovieSessionFilteringDto filteringParams) {
        List<MovieSessionEntity> result = movieSessionManagerEntity.getMovieSessions();

        if (filteringParams.getGenre() != null) {  // genre
            result = result.stream()
                    .filter(x -> x.getMovie().getGenre().equals(filteringParams.getGenre()))
                    .toList();
        }
        if (filteringParams.getAgeRating() != null) {  // age rating
            result = result.stream()
                    .filter(x -> x.getMovie().getAgeRating().equals(filteringParams.getAgeRating()))
                    .toList();
        }
        if (filteringParams.getSessionStartTime() != null) {  // starting time (as hour in 24h format)
            result = result.stream()
                    .filter(x -> x.getSessionStartTime().getHour() > filteringParams.getSessionStartTime())
                    .toList();
        }
        if (filteringParams.getLanguage() != null) {  // language
            result = result.stream()
                    .filter(x -> x.getSessionLanguage().equals(filteringParams.getLanguage()))
                    .toList();
        }
        result = result.stream()
                .sorted(Comparator.comparing(MovieSessionEntity::getSessionStartTime))
                .toList();
        return movieSessionMapper.toDtoList(result);
    }

}
