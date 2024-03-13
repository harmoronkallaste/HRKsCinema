package com.cgi.kinoapp.info;

import com.cgi.kinoapp.model.Screenings;
import com.cgi.kinoapp.model.Movies;
import com.cgi.kinoapp.model.Rooms;
import com.cgi.kinoapp.model.Seats;

public class ScreeningInfo {
    private Screenings screening;
    private Movies movie;
    private Rooms room;
    private Seats seat;

    public ScreeningInfo(Screenings screening, Movies movie, Rooms room) {
        this.screening = screening;
        this.movie = movie;
        this.room = room;
    }

    public ScreeningInfo(Screenings screening, Rooms room) {
        this.screening = screening;
        this.room = room;
    }



    public Screenings getScreening() {
        return screening;
    }

    public Movies getMovie() {
        return movie;
    }

    public Rooms getRoom() {
        return room;
    }
}
