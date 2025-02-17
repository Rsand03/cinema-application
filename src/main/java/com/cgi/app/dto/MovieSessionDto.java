package com.cgi.app.dto;

import lombok.Data;

@Data
public class MovieSessionDto {

    private Integer id;
    private MovieDto movie;
    private String language;
    private String startingTime;
    private String asString;

}
