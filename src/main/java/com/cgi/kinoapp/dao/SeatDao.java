package com.cgi.kinoapp.dao;

import com.cgi.kinoapp.model.Seats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatDao extends JpaRepository<Seats, Integer> {

    Optional<Seats> findSeatByRoomIdAndRowNumberAndSeatNumber(Integer roomId,
                                      Integer rowNumber, Integer seatNumber);

}
