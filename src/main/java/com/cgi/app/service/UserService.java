package com.cgi.app.service;

import com.cgi.app.entity.movie.MovieSessionEntity;
import com.cgi.app.entity.user.UserEntity;
import com.cgi.app.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    // Mocked repository functionality without DB
    private final UserEntity userEntity = UserRepository.userEntity;

    /**
     * Update User's watched genres HashMap and log watched movie title.
     * @param movieSession new watched movie
     * */
    public void addToWatchingHistory(MovieSessionEntity movieSession) {
        // Mocked repository functionality
        userEntity.addToWatchingHistory(movieSession);
    }

}
