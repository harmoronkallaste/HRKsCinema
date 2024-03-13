package com.cgi.kinoapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String room_name;
    private Integer basicSeatPrice;
    private Integer vipSeatPrice;
    private Integer totalSeats;

}
