package com.cgi.app.dto;

import lombok.Data;

@Data
public class MovieSessionFilteringDto {

    private String genre;
    private String ageRating;
    private Integer sessionStartTime;
    private String language;

}
