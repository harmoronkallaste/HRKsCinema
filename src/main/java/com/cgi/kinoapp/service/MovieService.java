package com.cgi.kinoapp.service;

import com.cgi.kinoapp.dao.MovieDao;
import com.cgi.kinoapp.model.Movies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieDao movieDao;

    public Optional<Movies> getMovieById(Integer id) {
        return movieDao.findById(id);
    }

    public List<Movies> getAllMovies() {
        return movieDao.findAll();
    }

    public List<Movies> getFirstTenMovies() {
        return movieDao.findFirst10();
    }
}
