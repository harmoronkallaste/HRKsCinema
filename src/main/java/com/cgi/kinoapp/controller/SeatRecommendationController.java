package com.cgi.kinoapp.controller;

import com.cgi.kinoapp.model.Seating;
import com.cgi.kinoapp.info.SeatRequest;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("recommendation")
public class SeatRecommendationController {

    @PostMapping("/occupancy")
    public int[][] getCurrentOccupancy(HttpSession session) {
        return generateRandomMatrix(session);
    }

    @PostMapping("/recommendSeats")
    public Seating getBestSeatingCombo(@RequestBody SeatRequest seatRequest, HttpSession session) {
        // If n is even:
        //    - if n = 2, we store the first seat of the best combo
        //    - if n >= 4, we store the n/2 seat of the best combo
        // If n is odd:
        //    - we store the middle seat of the best combo
        int amountOfSeats = seatRequest.getAmountOfSeats();
        int[][] matrix = getCurrentOccupancy(session);
        Map<Integer, List<Integer>> occupiedSeats = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            List<Integer> seatNumbers = new ArrayList<>();
            occupiedSeats.put(i, seatNumbers);
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    occupiedSeats.get(i).add(j);
                }
            }
        }
        /*for (int[] ints : matrix) {
            System.out.println(Arrays.toString(ints));
        }*/
        double minDistance = Double.MAX_VALUE;
        int bestRow = -1;
        int centreOfBestSeatCombo = -1;
        Map<Integer, Integer> bestIndexesForEachRow = new HashMap<>();
        for (int i = 0; i <= matrix.length-1; i++) {
            int bestIndex = subList(matrix[i], amountOfSeats);
            bestIndexesForEachRow.put(i, bestIndex);
        }
        int middleRow = matrix.length / 2;
        int middleColumn = matrix[middleRow].length / 2;
        for (Integer key : bestIndexesForEachRow.keySet()) {
            Integer value = bestIndexesForEachRow.get(key);
            double distanceFromMiddle = calculateDistance(key, value, middleRow, middleColumn);
            if (distanceFromMiddle < minDistance) {
                minDistance = distanceFromMiddle;
                centreOfBestSeatCombo = value;
                bestRow = key;
            }
        }
        Map<Integer, List<Integer>> bestSeatCombo = new HashMap<>();
        List<Integer> seatNumbers = new ArrayList<>();
        if (amountOfSeats%2==1) {
            int sub_add = (amountOfSeats-1)/2;
            for (int i = centreOfBestSeatCombo-sub_add; i <= centreOfBestSeatCombo+sub_add ; i++) {
                seatNumbers.add(i);
            }
        }
        else {
            int add = (amountOfSeats/2);
            for (int i = centreOfBestSeatCombo-(add-1); i <= centreOfBestSeatCombo+add; i++) {
                seatNumbers.add(i);
            }
        }
        bestSeatCombo.put(bestRow, seatNumbers);
        Seating dataForSeating = new Seating(occupiedSeats, bestSeatCombo);
        return dataForSeating;
    }

    private int[][] generateRandomMatrix(HttpSession session) {
        int[][] matrix = (int[][]) session.getAttribute("occupancyMatrix");
        if (matrix == null) {
            matrix = new int[10][10];
            Random random = new Random();
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    matrix[i][j] = random.nextInt(2); // Randomly mark seats as occupied
                }
            }
            session.setAttribute("occupancyMatrix", matrix);
        }
        return matrix;
    }

    public static int subList(int[] row, int n) {
        int comparator = row.length / 2;
        int bestMiddlePoint = 0;
        boolean middleExist = false;
        for (int i = 0; i <= (row.length - 1) - n + 1; i++) {
            int[] sub = Arrays.stream(row)
                    .boxed()
                    .collect(Collectors.toList())
                    .subList(i, i+n)
                    .stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
            if (Arrays.stream(sub).sum() == 0) {
                if (middleExist == false) {
                    if (n % 2 == 1) bestMiddlePoint = i+(n/2);
                    else {
                        bestMiddlePoint = i + (n-2) - 1;
                    }
                    middleExist = true;
                }
                else {
                    if (n % 2 == 1) {
                        if (Math.abs(comparator - (i+2)) < Math.abs(comparator - (bestMiddlePoint + 1))) {
                            bestMiddlePoint = i+(n/2);
                        }
                    }
                    else {
                        if (n >= 2) {
                            if (Math.abs(comparator - (i+1)) < Math.abs(comparator - (bestMiddlePoint + 1))) {
                                bestMiddlePoint = i;
                            }
                        }
                        else {
                            if (Math.abs(comparator - (i+1+(n/2-1))) < Math.abs(comparator - (bestMiddlePoint + 1))) {
                                bestMiddlePoint = i;
                            }
                        }
                    }
                }
            }
        }
        if (middleExist) {
            return bestMiddlePoint;
        }
        return -1;
    }


    public static double calculateDistance(int x1, int y1, int x2, int y2) {
        // Here I use the Euclidean distance formula.
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}
