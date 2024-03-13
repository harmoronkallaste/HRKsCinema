package com.cgi.kinoapp.controller;

import com.cgi.kinoapp.info.MovieInfo;
import com.cgi.kinoapp.info.ScreeningInfo;
import com.cgi.kinoapp.model.Movies;
import com.cgi.kinoapp.model.Screenings;
import com.cgi.kinoapp.service.MovieService;
import com.cgi.kinoapp.service.ReservationService;
import com.cgi.kinoapp.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("screenings")
public class ScreeningController {

    @Autowired
    private ScreenService screenService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MovieService movieService;

    @GetMapping("/allScreenings")
    public ResponseEntity<List<ScreeningInfo>> getAllScreeningsWithInfo() {
        List<Screenings> screenings = screenService.getAllScreenings();
        List<ScreeningInfo> screeningInfos = screenings.stream()
                .map(screening -> screenService.getScreeningInfo(screening.getId()).getBody())
                .collect(Collectors.toList());
        return ResponseEntity.ok(screeningInfos);
    }

    @GetMapping("/allScreeningTimesByMovies")
    public ResponseEntity<Map<Movies, List<ScreeningInfo>>> getAllScreeningsGroupedByMovie() {
        return ResponseEntity.ok(screenService.getAllScreeningTimesByMovie());
    }

    @GetMapping("idsearch/{id}")
    public ResponseEntity<Screenings> getScreeningById(@PathVariable Integer id) {
        Optional<Screenings> screeningOptional = screenService.getScreeningById(id);
        if (screeningOptional.isPresent()) return ResponseEntity.ok(screeningOptional.get());
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("info/{id}")
    public ResponseEntity<ScreeningInfo> getScreeningInfo(@PathVariable Integer id) {
        return screenService.getScreeningInfo(id);
    }

    @PutMapping("{screeningId}/reserve")
    public ResponseEntity<String> reserveSeats(@PathVariable Integer screeningId, @RequestBody List<Integer> seatIds) {
        if (reservationService.reserveSeats(screeningId, seatIds)) {
            if (seatIds.size() == 1) {
                return ResponseEntity.ok("Seat reserved successfully!");
            } else {
                return ResponseEntity.ok(seatIds.size() + " seats reserved successfully!");
            }
        } else {
            return ResponseEntity.badRequest().body("Unable to reserve seats.");
        }
    }

    // curl -X PUT http://localhost:8080/screenings/1/reserve -H "Content-Type: application/json" -d '[0, 1, 90]'

    @GetMapping("movie/{movieId}")
    public ResponseEntity<MovieInfo> oneMoviesScreenings(@PathVariable Integer movieId) {
        MovieInfo initial = new MovieInfo();
        Optional<Movies> moviesOptional = movieService.getMovieById(movieId);
        if (moviesOptional.isPresent()) {
            Movies movie = moviesOptional.get();
            List<ScreeningInfo> forMovie = new ArrayList<>();
            List<ScreeningInfo> screenings = getAllScreeningsWithInfo().getBody();
            for (ScreeningInfo screening : screenings) {
                if (screening.getScreening().getMovieId() == movieId) {
                    forMovie.add(screening);
                }
            }
            initial = new MovieInfo(movie, forMovie);
        }
        return ResponseEntity.ok(initial);
    }


}
