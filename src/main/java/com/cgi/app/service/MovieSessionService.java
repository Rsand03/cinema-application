package com.cgi.app.service;

import com.cgi.app.dto.MovieSessionDto;
import com.cgi.app.dto.mapper.MovieMapper;
import com.cgi.app.dto.mapper.MovieSessionMapper;
import com.cgi.app.entity.movie.MovieSessionEntity;
import com.cgi.app.entity.movie.MovieSessionManager;
import com.cgi.app.repository.MovieSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieSessionService {

    private final MovieSessionManager movieSessionManagerEntity = MovieSessionRepository.movieSessionManager;
    private final MovieMapper movieMapper;
    private final MovieSessionMapper movieSessionMapper;

    /**
     * Get data about all available movie sessions.
     */
    public List<MovieSessionDto> getAvailableMovieSessions() {
        return movieSessionMapper.toDtoList(movieSessionManagerEntity.getMovieSessions());

    }

    /**
     * Get movie session by its id if the session present.
     * @param id id of the movie
     * @return Optional object possibly containing a movie
     * */
    public Optional<MovieSessionEntity> getMovieSessionById(int id) {
        return movieSessionManagerEntity.getMovieSessions().stream()
                .filter(x -> x.getSessionId() == id)
                .findFirst();
    }

    /**
     * Filter available movie sessions by genre, age rating, starting time and language.
     * TODO: Make front-end dropdown menus (that contain filtering options) dynamic.
     * @param genre required genre
     * @param ageRating required age rating
     * @param sessionStartTime minimum starting time (as hour in 24h format)
     * @param language required language
     * @return List containing filtered movies.
     * */
    public List<MovieSessionDto> getFilteredMovieSessions(String genre, String ageRating, String sessionStartTime, String language) {
        List<MovieSessionEntity> result = movieSessionManagerEntity.getMovieSessions();

        // value "-" means that the specific category must not be filtered
        if (!"-".equals(genre)) {  // genre
            result = result.stream()
                    .filter(x -> x.getMovie().getGenre().equals(genre))
                    .toList();
        }
        if (!"-".equals(ageRating)) {  // age rating
            result = result.stream()
                    .filter(x -> x.getMovie().getAgeRating().equals(ageRating))
                    .toList();
        }
        if (!"-".equals(sessionStartTime)) {  // starting time (as hour in 24h format)
            int hour = Integer.parseInt(sessionStartTime);
            result = result.stream()
                    .filter(x -> x.getSessionStartTime().getHour() > hour)
                    .toList();
        }
        if (!"-".equals(language)) {  // language
            result = result.stream()
                    .filter(x -> x.getSessionLanguage().equals(language))
                    .toList();
        }
        result = result.stream()
                .sorted(Comparator.comparing(MovieSessionEntity::getSessionStartTime))
                .toList();
        return movieSessionMapper.toDtoList(result);
    }

}
