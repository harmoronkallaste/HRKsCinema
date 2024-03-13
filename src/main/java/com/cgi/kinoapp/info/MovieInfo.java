package com.cgi.kinoapp.info;

import com.cgi.kinoapp.model.Movies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieInfo {
    private Movies movie;
    private List<ScreeningInfo> screenings;

    public MovieInfo(Movies movie, List<ScreeningInfo> screenings) {
        this.movie = movie;
        this.screenings = screenings;
    }

    public MovieInfo() {

    }

    public Movies getMovie() {
        return movie;
    }

    public void setMovie(Movies movie) {
        this.movie = movie;
    }

    public List<ScreeningInfo> getScreenings() {
        return screenings;
    }

    public void setScreenings(List<ScreeningInfo> screenings) {
        this.screenings = screenings;
    }
}
