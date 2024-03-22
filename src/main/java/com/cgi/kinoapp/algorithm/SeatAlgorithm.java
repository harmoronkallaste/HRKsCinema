package com.cgi.kinoapp.algorithm;

import java.util.*;
import java.util.stream.Collectors;

public class SeatAlgorithm {

    /*
     *
     * This is the seating recommendation algorithm that I came up with. I did not use any external help for this.
     * If I had one more day for this assignment I would definitely make it much more optimized, refactor it
     * and make better documentation for it, but this will have to do for now.
     *
     * Since the assignment description said that the occupied seats for screenings should be randomised,
     * I firstly created a matrix of 1s and 0s that were placed randomly into the matrix.
     *
     * Then for each row in the matrix we check it like this:
     * - based on the amount of seats that need to be recommended we iterate over all the elements in the row
     * - from indexes [0, matrix[i].length - n] and check if the sub list from [j, j+n) for each j in matrix[i]
     * - has a sum of 0. This works because unoccupied seats are marked as 0 so if the sum = 0 then we have found
     * - the desired amount of unoccupied seats in that row.
     * - If the row has multiple options for the desired amount of unoccupied seats, we check which one is horizontally
     * - more in the centre. I came up with the mathematical side of this on paper, but basically the logic for even
     * - and odd numbers is a little different.
     * Our previous work has given us the best options for the desired amount of unoccupied seats for each row.
     * Now we have to check which one of those has it's middle seat closest to the best seat in the room.
     * By the descriptions in the assignment the best seat in the house is in the middle of the middle row of the room.
     * Once we have the best recommendation for the user, we print the seating plan out with the recommended seats.
     */




    private static int[][] generateRandomOccupancy(int rows, int columns) {
        // Initialise a matrix of 1s and 0s with the desired dimensions.
        int[][] matrix = new int[rows][columns];
        Random random = new Random();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                // For every entry matrix[i][j] let's randomly
                // put either a 1 or a 0 there.
                matrix[i][j] = random.nextInt(2);
            }
        }
        return matrix;
    }

    public static int bestMiddleSeatForRow(int[] row, int n) {
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

    public static double eucledianDistance(int x1, int y1, int x2, int y2) {
        // Calculate the distance between the best seat in the room and the
        // middle seat of the seat combo that we're currently checking.
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static Map<Integer, List<Integer>> getBestSeatingCombo(int[][] matrix, int amountOfSeats) {
        // If n is even:
        //    - we store n/2 seat of the best combo
        // If n is odd:
        //    - we store the middle seat of the best combo
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
        double minDistance = Double.MAX_VALUE;
        int bestRow = -1;
        int centreOfBestSeatCombo = -1;
        Map<Integer, Integer> bestIndexesForEachRow = new HashMap<>();
        for (int i = 0; i <= matrix.length-1; i++) {
            int bestIndex = bestMiddleSeatForRow(matrix[i], amountOfSeats);
            bestIndexesForEachRow.put(i, bestIndex);
        }
        int middleRow = matrix.length / 2;
        int middleColumn = matrix[middleRow].length / 2;
        for (Integer key : bestIndexesForEachRow.keySet()) {
            Integer value = bestIndexesForEachRow.get(key);
            double distanceFromMiddle = eucledianDistance(key, value, middleRow, middleColumn);
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
        Map<Integer, List<Integer>> dataForSeating = bestSeatCombo;
        return dataForSeating;
    }

    public static void main(String[] args) {
        // Get the randomised seating plan
        int[][] seatingPlan = generateRandomOccupancy(10,10);
        // Print out the initial seating plan
        System.out.println("The randomised seating plan:");
        System.out.println("---------------------------------------------");
        for (int i = 0; i < seatingPlan.length; i++) {
            for (int j = 0; j < seatingPlan[i].length; j++) {
                System.out.print(seatingPlan[i][j] + " ");
            }
            System.out.println();
        }
        // Ask the user how many seats they want
        System.out.println("---------------------------------------------");
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many seats would you like?: ");
        int amountOfSeats = Integer.valueOf(scanner.nextLine());
        scanner.close();
        // Print out the seating plan with recommended seats as "-"
        System.out.println("Seating plan with recommended seats");
        Map<Integer, List<Integer>> recommendedSeats = getBestSeatingCombo(seatingPlan, amountOfSeats);
        for (int i = 0; i < seatingPlan.length; i++) {
            List<Integer> seatNumbers = new ArrayList<>();
            if (recommendedSeats.containsKey(i)) {
                seatNumbers = recommendedSeats.get(i);
            }
            for (int j = 0; j < seatingPlan[i].length; j++) {
                if (seatNumbers.size() > 0) {
                    if (seatNumbers.contains(j)) System.out.print("- ");
                    else System.out.print(seatingPlan[i][j] + " ");
                }
                else System.out.print(seatingPlan[i][j] + " ");
            }
            System.out.println();
        }

    }

}

