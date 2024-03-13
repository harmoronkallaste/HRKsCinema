package com.cgi.kinoapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Seats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer roomId;
    private Integer rowNumber;
    private Integer seatNumber;
    private Integer vip;
}
