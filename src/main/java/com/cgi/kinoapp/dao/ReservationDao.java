package com.cgi.kinoapp.dao;

import com.cgi.kinoapp.model.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationDao extends JpaRepository<Reservations, Integer> {

    Optional<Reservations> findById(Integer id);

    List<Reservations> findByScreeningIdAndSeatId(int screeningId, int seatId);

    boolean existsByScreeningIdAndSeatIdIn(Integer screeningId, List<Integer> seatIds);

    List<Reservations> findAllByPurchaseId(Integer purchaseId);

    List<Reservations> findByScreeningId(Integer screeningId);
}
