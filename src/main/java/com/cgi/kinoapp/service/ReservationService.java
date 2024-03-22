package com.cgi.kinoapp.service;


import com.cgi.kinoapp.dao.EmailSender;
import com.cgi.kinoapp.dao.ReservationDao;
import com.cgi.kinoapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private RoomService roomService;
    @Autowired
    private SeatService seatService;
    @Autowired
    private ScreenService screenService;
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private EmailSender emailSender;

    public Optional<Reservations> getReservationById(Integer id) {
        return reservationDao.findById(id);
    }

    public boolean isSeatAvailableForScreening(int screeningId, int seatId) {
        List<Reservations> existingReservations = reservationDao.findByScreeningIdAndSeatId(screeningId, seatId);
        return existingReservations.isEmpty(); // If no reservations found, seat is available
    }

    public boolean reserveSeats(Integer screeningId, List<Integer> seatIds) {
        try {

            Optional<Screenings> screeningOptional = screenService.getScreeningById(screeningId);
            if (screeningOptional.isPresent()) {
                Screenings screening = screeningOptional.get();
                BigDecimal paymentAmount = calculatePaymentAmount(screening.getRoomId(), seatIds);

                boolean seatsAlreadyReserved = reservationDao.existsByScreeningIdAndSeatIdIn(screeningId, seatIds);
                if (seatsAlreadyReserved) {
                    return false; // Seats are already reserved, return false
                }

                Purchases purchase = purchaseService.addPurchase(paymentAmount);
                // Timestamp currentTime = new Timestamp(System.currentTimeMillis());

                for (Integer seatId : seatIds) {

                    if (!isSeatAvailableForScreening(screeningId, seatId)) {
                        return false;
                    }

                    Reservations reservation = new Reservations();

                    reservation.setScreeningId((screeningId));
                    reservation.setSeatId(seatId);
                    reservation.setReservationTime(screening.getStartTime());
                    reservation.setSeatCost(calculateSeatCost(screening.getRoomId(), seatId));
                    reservation.setPurchaseId(purchase.getId());
                    // reservation.setConfirmedAt(currentTime);
                    reservationDao.save(reservation);

                }

                emailSender.sendPurchaseConfirmationEmail(purchase.getId(), "lisell.aare@gmail.com");

                return true;

            }

            else {
                return false;
            }

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private BigDecimal calculatePaymentAmount(Integer roomId, List<Integer> seatIds) {
        Optional<Rooms> roomOptional = roomService.getRoomById(roomId);
        BigDecimal paymentTotal = BigDecimal.ZERO;
        if (roomOptional.isPresent()) {
            Rooms room = roomOptional.get();

            for (Integer seatId : seatIds) {

                Optional<Seats> seatsOptional = seatService.getSeatById(seatId);
                Seats seat = seatsOptional.get();
                Integer vip = seat.getVip();

                BigDecimal seatPrice;

                if (vip == 1) {
                    seatPrice = BigDecimal.valueOf(room.getVipSeatPrice());
                }

                else {
                    seatPrice = BigDecimal.valueOf(room.getBasicSeatPrice());
                }

                paymentTotal = paymentTotal.add(seatPrice);

            }

            return paymentTotal;
        } else {
            return BigDecimal.ZERO;
        }

    }

    private BigDecimal calculateSeatCost(Integer roomId, Integer seatId) {
        Optional<Rooms> roomOptional = roomService.getRoomById(roomId);
        BigDecimal paymentTotal = BigDecimal.ZERO;
        if (roomOptional.isPresent()) {
            Rooms room = roomOptional.get();

                Optional<Seats> seatsOptional = seatService.getSeatById(seatId);
                Seats seat = seatsOptional.get();
                Integer vip = seat.getVip();

                BigDecimal seatPrice;

                if (vip == 1) {
                    seatPrice = BigDecimal.valueOf(room.getVipSeatPrice());
                }

                else {
                    seatPrice = BigDecimal.valueOf(room.getBasicSeatPrice());
                }

                paymentTotal = seatPrice;

            return paymentTotal;
        } else {
            return BigDecimal.ZERO;
        }

    }

}
