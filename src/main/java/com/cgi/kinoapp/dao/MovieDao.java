package com.cgi.kinoapp.dao;

import com.cgi.kinoapp.model.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieDao extends JpaRepository<Movies, Integer> {

    Optional<Movies> findById(Integer id);

    @Query("SELECT m FROM Movies m ORDER BY m.id ASC LIMIT 10")
    List<Movies> findFirst10();
}
