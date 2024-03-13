package com.cgi.kinoapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.sql.Timestamp;

@Data
@Entity
public class Screenings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer movieId;
    private Integer roomId;
    private Timestamp startTime;
    private Timestamp endTime;
}
