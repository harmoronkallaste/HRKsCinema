package com.cgi.kinoapp.controller;

import com.cgi.kinoapp.model.Reservations;
import com.cgi.kinoapp.model.Screenings;
import com.cgi.kinoapp.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("idsearch/{id}")
    public ResponseEntity<Reservations> getReservationById(@PathVariable Integer id) {
        Optional<Reservations> reservationOptional = reservationService.getReservationById(id);
        if (reservationOptional.isPresent()) return ResponseEntity.ok(reservationOptional.get());
        else return ResponseEntity.notFound().build();
    }

}
