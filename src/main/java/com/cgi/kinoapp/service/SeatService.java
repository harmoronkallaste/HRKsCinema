package com.cgi.kinoapp.service;

import com.cgi.kinoapp.dao.ReservationDao;
import com.cgi.kinoapp.model.Seats;
import com.cgi.kinoapp.dao.SeatDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    @Autowired
    SeatDao seatDao;

    public Optional<Seats> findSeatByRoomIdAndRowNumberAndSeatNumber(Integer roomId,
                                                             Integer rowNumber, Integer seatNumber)

    {

        return seatDao.findSeatByRoomIdAndRowNumberAndSeatNumber(roomId,rowNumber,seatNumber);

    }

    public Optional<Seats> getSeatById(Integer id) {
        return seatDao.findById(id);
    }



}

