package com.cgi.kinoapp.dao;

import com.cgi.kinoapp.model.Screenings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScreeningDao extends JpaRepository<Screenings, Integer> {

    Optional<Screenings> findById(Integer id);

    List<Screenings> findAll();

}
