package com.cgi.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieSessionAttributesDto {

    private List<String> genres;
    private List<String> ageRatings;
    private List<String> sessionStartingTimes;
    private List<String> languages;

}
