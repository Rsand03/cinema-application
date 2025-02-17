package com.cgi.app.service;

import com.cgi.app.entity.movie.MovieSessionEntity;
import com.cgi.app.entity.user.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserEntity user = new UserEntity();


    /**
     * Update User's watched genres HashMap and log watched movie title.
     * @param movieSession new watched movie
     * */
    public void addToWatchingHistory(MovieSessionEntity movieSession) {
        user.addToWatchingHistory(movieSession);
    }

}
