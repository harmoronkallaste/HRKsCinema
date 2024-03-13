package com.cgi.kinoapp.dao;

import com.cgi.kinoapp.model.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDao extends JpaRepository<Rooms, Integer> {

    List<Rooms> findByBasicSeatPrice(Integer bsp);

    List<Rooms> findByVipSeatPrice(Integer bsp);
}
