package com.cgi.app.controller;

import com.cgi.app.dto.SeatDto;
import com.cgi.app.entity.movie.MovieSessionManager;
import com.cgi.app.entity.movie.MovieSessionEntity;
import com.cgi.app.entity.user.UserEntity;
import com.cgi.app.service.MovieSessionService;
import com.cgi.app.service.SeatingPlanService;
import com.cgi.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SeatController {

    private final SeatingPlanService seatingPlanService;

    /**
     * Get seating plan data.
     * Http status 200: found neighbouring seats
     * Http status 206: found seats, but not neighbouring
     *
     * @param ticketCount amount of seats to select
     * @return seating plan in json format, HashMap for each seat:
     * "seatNumber": seat number
     * "occupationStatus": state of the seat (FREE / SELECTED / OCCUPIED)
     */
    @RequestMapping("/seats")
    public ResponseEntity<List<SeatDto>> getRandomSeatingPlan(@RequestParam Integer ticketCount) {
        return seatingPlanService.getSeatingPlanWithNeighbouringSeats(ticketCount)
                .map(ResponseEntity::ok)
                .orElseGet(() -> seatingPlanService.getAnySeatingPlan(ticketCount)
                        .map(seatingPlanWithAnySeats ->
                                new ResponseEntity<>(seatingPlanWithAnySeats, HttpStatus.PARTIAL_CONTENT))
                        .orElseGet(() ->
                                new ResponseEntity<>(HttpStatus.NO_CONTENT)
                        ));
    }

}
