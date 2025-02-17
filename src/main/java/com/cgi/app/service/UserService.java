package com.cgi.app.service;

import com.cgi.app.dto.MovieSessionDto;
import com.cgi.app.dto.mapper.MovieSessionMapper;
import com.cgi.app.entity.movie.MovieSessionEntity;
import com.cgi.app.entity.movie.MovieSessionManager;
import com.cgi.app.entity.user.UserEntity;
import com.cgi.app.repository.MovieSessionRepository;
import com.cgi.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static com.cgi.app.util.Constants.RECOMMENDED_MOVIE_SESSIONS_AMOUNT;

@RequiredArgsConstructor
@Service
public class UserService {

    // Mocked repository functionality without DB
    private static final UserEntity userEntity = UserRepository.userEntity;
    private static final MovieSessionManager movieSessionManagerEntity = MovieSessionRepository.movieSessionManager;
    private final MovieSessionMapper movieSessionMapper;


    /**
     * Update User's watched genres HashMap and log watched movie title.
     *
     * @param movieSession new watched movie
     */
    public void addToWatchingHistory(MovieSessionEntity movieSession) {
        userEntity.addToWatchingHistory(movieSession);
    }

    /**
     * Determine how many times the genre of the movie has been watched.
     *
     * @param movieSession movie to be checked
     * @return time genre watched
     */
    private Integer getTimesGenreWatched(MovieSessionEntity movieSession) {
        return userEntity.getGenresWatched().get(movieSession.getMovie().getGenre());
    }

    /**
     * Recommend new movie sessions based on user's watching history and watched genres.
     *
     * @return top 5 most fitting movies
     */
    public List<MovieSessionDto> getTopFiveRecommendedMovies() {
        List<String> alreadyWatchedMovies = userEntity.getAlreadyWatchedMovies();
        if (alreadyWatchedMovies.isEmpty()) {
            return List.of();
        }

        List<MovieSessionEntity> topRecommendedMovies = movieSessionManagerEntity.getMovieSessions().stream()
                .filter(x -> !alreadyWatchedMovies.contains(x.getMovie().getTitle()))  // filter out already watched movies
                .sorted(Comparator
                        .comparing(this::getTimesGenreWatched))
                .toList()
                .reversed();
        if (topRecommendedMovies.size() <= RECOMMENDED_MOVIE_SESSIONS_AMOUNT) {
            return movieSessionMapper.toDtoList(topRecommendedMovies);
        }
        return movieSessionMapper.toDtoList(topRecommendedMovies.subList(0, RECOMMENDED_MOVIE_SESSIONS_AMOUNT + 1));
    }

}
