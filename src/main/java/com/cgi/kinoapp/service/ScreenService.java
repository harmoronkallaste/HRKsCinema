package com.cgi.kinoapp.service;

import com.cgi.kinoapp.info.ScreeningInfo;
import com.cgi.kinoapp.info.MovieInfo;
import com.cgi.kinoapp.dao.ScreeningDao;
import com.cgi.kinoapp.model.Movies;
import com.cgi.kinoapp.model.Rooms;
import com.cgi.kinoapp.model.Screenings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ScreenService {

    @Autowired
    private ScreeningDao screeningDao;
    @Autowired
    private MovieService movieService;

    @Autowired
    private RoomService roomService;

    public Optional<Screenings> getScreeningById(Integer id) {
        return screeningDao.findById(id);
    }

    public List<Screenings> getAllScreenings() {
        return screeningDao.findAll();
    }

    public Map<Movies, List<ScreeningInfo>> getAllScreeningTimesByMovie() {
        Map<Movies, List<ScreeningInfo>> map = new HashMap<>();
        List<Screenings> allScreenings = getAllScreenings();
        for (Screenings screening : allScreenings) {
            Integer movieID = screening.getMovieId();
            Optional<Movies> moviesOptional = movieService.getMovieById(movieID);
            if (moviesOptional.isPresent()) {
                Movies movie = moviesOptional.get();
                if (map.containsKey(movie)) {
                    Integer roomID = screening.getRoomId();
                    Optional<Rooms> roomsOptional = roomService.getRoomById(roomID);
                    if (roomsOptional.isPresent()) {
                        Rooms room = roomsOptional.get();
                        ScreeningInfo screeningInfo = new ScreeningInfo(screening, room);
                        map.get(movie).add(screeningInfo);
                    }

                } else {
                    List<ScreeningInfo> screeningsForMovie = new ArrayList<>();
                    Integer roomID = screening.getRoomId();
                    Optional<Rooms> roomsOptional = roomService.getRoomById(roomID);
                    if (roomsOptional.isPresent()) {
                        Rooms room = roomsOptional.get();
                        ScreeningInfo screeningInfo = new ScreeningInfo(screening, room);
                        screeningsForMovie.add(screeningInfo);
                        map.put(movie, screeningsForMovie);
                    }
                }
            }
        }
        return map;
    }

    public ResponseEntity<ScreeningInfo> getScreeningInfo(Integer id) {

        Optional<Screenings> screeningsOptional = screeningDao.findById(id);
        if (screeningsOptional.isPresent()) {
            Screenings screening = screeningsOptional.get();
            Optional<Movies> movieOptional = movieService.getMovieById(screening.getMovieId());
            Optional<Rooms> roomOptional = roomService.getRoomById(screening.getRoomId());
            if (movieOptional.isPresent() && roomOptional.isPresent()) {
                Movies movie = movieOptional.get();
                Rooms room = roomOptional.get();

                ScreeningInfo screeningInfo = new ScreeningInfo(screening, movie, room);
                return ResponseEntity.ok(screeningInfo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }

    }


}
