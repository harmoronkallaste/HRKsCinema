package com.cgi.kinoapp.model;


import java.util.List;
import java.util.Map;

public class Seating {

    private Map<Integer, List<Integer>> occupiedSeats;
    private Map<Integer, List<Integer>> recommendedSeats;

    public Seating(Map<Integer, List<Integer>> occupiedSeats, Map<Integer, List<Integer>> recommendedSeats) {
        this.occupiedSeats = occupiedSeats;
        this.recommendedSeats = recommendedSeats;
    }

    public Map<Integer, List<Integer>> getOccupiedSeats() {
        return occupiedSeats;
    }

    public void setOccupiedSeats(Map<Integer, List<Integer>> occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
    }

    public Map<Integer, List<Integer>> getRecommendedSeats() {
        return recommendedSeats;
    }

    public void setRecommendedSeats(Map<Integer, List<Integer>> recommendedSeats) {
        this.recommendedSeats = recommendedSeats;
    }

}
