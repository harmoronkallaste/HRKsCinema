package com.cgi.kinoapp.controller;

import com.cgi.kinoapp.model.Movies;
import com.cgi.kinoapp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("idsearch/{id}")
    public ResponseEntity<Movies> getMovieById(@PathVariable Integer id) {
        Optional<Movies> movieOptional = movieService.getMovieById(id);
        if (movieOptional.isPresent()) {
            return ResponseEntity.ok(movieOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("all")
    public ResponseEntity<List<Movies>> getAllMovies() {
        List<Movies> allMovies = movieService.getAllMovies();
        return ResponseEntity.ok(allMovies);
    }

    @GetMapping("first10")
    public ResponseEntity<List<Movies>> getFirstTenMovies() {
        List<Movies> firstTenMovies = movieService.getFirstTenMovies();
        return ResponseEntity.ok(firstTenMovies);
    }
}

