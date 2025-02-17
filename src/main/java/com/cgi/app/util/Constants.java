package com.cgi.app.util;

import java.util.List;

public class Constants {

    private Constants() {
    }

    // --- constants related to the seats and seating plan in the cinema ---
    public static final double SEAT_BEING_OCCUPIED_IN_SEATING_PLAN_PROBABILITY = 0.5;
    // changing the number of seats would currently break front-end
    public static final int SEAT_ROWS_COUNT = 9;  // should be an odd number
    public static final int SEAT_COLUMNS_COUNT = 15;  // should be an odd number
    public static final int CENTRE_SEAT_ROW_NUMBER = (SEAT_ROWS_COUNT / 2) + 1;  // indexing starts at 1
    public static final int CENTRE_SEAT_COLUMN_NUMBER = (SEAT_COLUMNS_COUNT / 2) + 1;  // indexing starts at 1
    public static final int TOTAL_SEATS_COUNT = SEAT_COLUMNS_COUNT * SEAT_ROWS_COUNT;


    // --- constants related to the cinema ---
    public static final int TOTAL_AVAILABLE_MOVIE_SESSIONS_COUNT = 35;


    // --- constants related to movies ---
    // back-end supports modifying these lists, but front-end has static hardcoded dropdown menus
    public static final List<String> MOVIE_TITLES = List.of(
            "Barbie", "Oppenheimer", "Avengers", "The Ivory Game", "The Amazon",
            "Dont look up", "Scream", "Saw 1", "Saw 2", "Saw 3", "Saw 4", "Saw 5", "Saw 6"
    );
    public static final List<String> MOVIE_GENRES = List.of("Comedy", "Action", "Horror", "Nature");
    public static final List<String> MOVIE_AGE_RATINGS = List.of("G", "PG", "PG-13", "NC-17");


    // --- constants related to (movie) sessions ---
    // back-end supports modifying these lists, but front-end has static hardcoded dropdown menus
    public static final List<Integer> SESSION_STARTING_TIME_MINUTES = List.of(0, 15, 30, 45);
    public static final List<String> MOVIE_SESSION_LANGUAGES = List.of("Estonian", "English", "Russian");

}
