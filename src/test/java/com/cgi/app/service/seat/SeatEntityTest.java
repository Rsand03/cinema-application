package com.cgi.app.service.seat;

import com.cgi.app.entity.seat.SeatEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.cgi.app.entity.seat.SeatEntity.OccupationStatus.FREE;
import static com.cgi.app.entity.seat.SeatEntity.OccupationStatus.SELECTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeatEntityTest {

    private SeatEntity seat;

    @BeforeEach
    void beforeEach() {
        seat = new SeatEntity(1, 2, 5);
    }

    @Test
    void testSeat_simpleGetters() {
        assertEquals(1, seat.getRowNumber());
        assertEquals(2, seat.getSeatNumber());
        assertEquals(5, seat.getDistanceFromCenter());
    }

    @Test
    void testSeat_getOccupationStatus_returnsString() {
        assertInstanceOf(String.class, seat.getOccupationStatus());
    }

    @Test
    void testSeat_getOccupationStatus_isFreeAtFirst() {
        assertEquals(FREE.toString(), seat.getOccupationStatus());
    }

    @Test
    void testSeat_isFree() {
        assertEquals(FREE.toString(), seat.getOccupationStatus());
        assertTrue(seat.isFree());
    }

    @Test
    void testSeat_setOccupationStatus() {
        seat.setOccupationStatus(SELECTED);
        assertEquals(SELECTED.toString(), seat.getOccupationStatus());
    }

    @Test
    void testSeat_setRandomOccupationStatus_setsToFreeOrOccupied() {
        seat.setRandomOccupationStatus();
        assertTrue(seat.getOccupationStatus().equals("FREE") || seat.getOccupationStatus().equals("OCCUPIED"));
    }
}
