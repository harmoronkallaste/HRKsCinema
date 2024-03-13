package com.cgi.kinoapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
public class Reservations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer screeningId;
    private Integer seatId;
    private Timestamp reservationTime;
    private BigDecimal seatCost;
    private Integer purchaseId;
}
