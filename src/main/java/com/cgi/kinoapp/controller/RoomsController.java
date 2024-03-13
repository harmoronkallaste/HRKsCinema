package com.cgi.kinoapp.controller;

import com.cgi.kinoapp.model.Rooms;
import com.cgi.kinoapp.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("rooms")
public class RoomsController {

    @Autowired
    RoomService roomService;

    @GetMapping("allRooms")
    public List<Rooms> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("bsp/{bsp}")
    public List<Rooms> getRoomByBasicSeatPrice(@PathVariable Integer bsp) {
        return roomService.getRoomsByBasicSeatPrice(bsp);
    }

    @GetMapping("vsp/{vsp}")
    public List<Rooms> getRoomByVipSeatPrice(@PathVariable Integer vsp) {
        return roomService.getRoomsByVipSeatPrice(vsp);
    }

}
