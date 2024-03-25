package com.cgi.cinema;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CinemaController {

    @RequestMapping
    public String HelloWorld() {
        return "Hello world";
    }

}
