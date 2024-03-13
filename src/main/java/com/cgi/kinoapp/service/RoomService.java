package com.cgi.kinoapp.service;

import com.cgi.kinoapp.model.Rooms;
import com.cgi.kinoapp.dao.RoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    RoomDao roomDao;
    public List<Rooms> getAllRooms() {

        return roomDao.findAll();

    }

    public List<Rooms> getRoomsByBasicSeatPrice(Integer bsp) {

        return roomDao.findByBasicSeatPrice(bsp);

    }

    public List<Rooms> getRoomsByVipSeatPrice(Integer bsp) {

        return roomDao.findByVipSeatPrice(bsp);

    }

    public Optional<Rooms> getRoomById(Integer roomId) {
        return roomDao.findById(roomId);
    }
}
