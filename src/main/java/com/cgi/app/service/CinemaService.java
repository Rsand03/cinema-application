package com.cgi.app.service;

import com.cgi.app.entity.cinema.CinemaEntity;
import com.cgi.app.entity.movie.MovieSessionEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class CinemaService {

    private final CinemaEntity cinema = new CinemaEntity();

    /**
     * Get data about all available movie sessions.
     */
    public List<Map<String, String>> getAvailableMovieSessions() {
        return cinema.getMovieSessions().stream()
                .map(MovieSessionEntity::toJson)
                .toList();
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
    public List<Map<String, String>> getFilteredMovieSessions(String genre, String ageRating, String sessionStartTime, String language) {
        List<MovieSessionEntity> result = cinema.getMovieSessions();

        // value "-" means that the specific category must not be filtered
        if (!"-".equals(genre)) {  // genre
            result = result.stream()
                    .filter(x -> x.getGenre().equals(genre))
                    .toList();
        }
        if (!"-".equals(ageRating)) {  // age rating
            result = result.stream()
                    .filter(x -> x.getAgeRating().equals(ageRating))
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
                    .filter(x -> x.getLanguage().equals(language))
                    .toList();
        }
        result = result.stream()
                .sorted(Comparator.comparing(MovieSessionEntity::getSessionStartTime))
                .toList();
        return result.stream()
                .map(MovieSessionEntity::toJson)
                .toList();
    }
}
