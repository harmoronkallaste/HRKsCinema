package com.cgi.kinoapp.controller;

import com.cgi.kinoapp.model.Seats;
import com.cgi.kinoapp.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping("/{roomId}/{rowNumber}/{seatNumber}")
    public Optional<Seats> getSeatInfo(@PathVariable Integer roomId, @PathVariable Integer rowNumber,
                                       @PathVariable Integer seatNumber) {
        return seatService.findSeatByRoomIdAndRowNumberAndSeatNumber(roomId, rowNumber, seatNumber);

    }




    /*
    @PutMapping("/reserve")
    public ResponseEntity<String> reserveSeats(@RequestBody List<Integer> seatIds) {
        if (seatService.reserveSeats(seatIds)) {
            if (seatIds.size() == 1) {
                return ResponseEntity.ok("Seat reserved successfully!");
            } else {
                return ResponseEntity.ok(seatIds.size() + " seats reserved successfully!");
            }
        } else {
            return ResponseEntity.badRequest().body("Unable to reserve seats.");
        }
    }
    Testimiseks kasutasin curl -X PUT http://localhost:8080/seats/reserve -H "Content-Type: application/json" -d '[0, 2, 3]'
    curl -X PUT http://localhost:8080/seats/reserve -H "Content-Type: application/json" -d '[4]'
     */
}
