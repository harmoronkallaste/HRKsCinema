package com.cgi.kinoapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Movies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer ranking;
    private String movieName;
    private Integer releaseYear;
    private Double rating;
    private String genre;
    private String certificate;
    private String runTime;
    private String tagline;
    private Long budget;
    private Long boxOffice;
    private String casts;
    private String directors;
    private String writers;
    private String picUrl;

    public Integer getId() {
        return id;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getRunTime() {
        return runTime;
    }

}
