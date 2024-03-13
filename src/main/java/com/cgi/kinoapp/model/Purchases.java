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
public class Purchases {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal totalCost;
    private Timestamp confirmedAt;

}