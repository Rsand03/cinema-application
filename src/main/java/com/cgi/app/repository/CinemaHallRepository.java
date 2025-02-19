package com.cgi.app.repository;

import com.cgi.app.entity.seat.CinemaHallEntity;

public class CinemaHallRepository {

    private CinemaHallRepository() {
    }

    // Mocked repository functionality without DB
    public static final CinemaHallEntity cinemaHallEntity = new CinemaHallEntity();

}
