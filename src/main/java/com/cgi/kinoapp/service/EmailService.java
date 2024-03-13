package com.cgi.kinoapp.service;

import com.cgi.kinoapp.dao.EmailSender;
import com.cgi.kinoapp.info.ScreeningInfo;
import com.cgi.kinoapp.model.*;
import com.cgi.kinoapp.service.PurchaseService;
import com.cgi.kinoapp.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmailService implements EmailSender {
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ScreenService screenService;
    @Autowired
    private SeatService seatService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private RoomService roomService;

    public EmailService(PurchaseService purchaseService, JavaMailSender javaMailSender, ScreenService screenService, SeatService seatService, MovieService movieService) {
        this.purchaseService = purchaseService;
        this.javaMailSender = javaMailSender;
        this.screenService = screenService;
        this.seatService = seatService;
        this.movieService = movieService;
    }

    public void sendPurchaseConfirmationEmail(Integer purchaseId, String emailAddress) {
        Purchases purchase = purchaseService.getPurchaseById(purchaseId).orElse(null);
        if (purchase == null) {
            // Handle error: Purchase not found
            return;
        }

        List<Reservations> reservations = purchaseService.getReservationsByPurchaseId(purchaseId);

        String emailContent = constructEmailContent(purchase, reservations);

        sendEmail(emailAddress, "Purchase Confirmation", emailContent);
    }

    private String constructEmailContent(Purchases purchase, List<Reservations> reservations) {
        StringBuilder content = new StringBuilder();
        content.append("Thank you for your purchase!\n\n");
        content.append("Purchase ID: ").append(purchase.getId()).append("\n");
        content.append("Confirmed at: ").append(purchase.getConfirmedAt()).append("\n");
        content.append("Total cost: ").append(purchase.getTotalCost()).append(" €").append("\n");
        content.append("\n");
        content.append("Ticket(s):\n");
        content.append("\n");
        for (Reservations reservation : reservations) {
            Integer screeningID = reservation.getScreeningId();
            Integer seatID = reservation.getSeatId();
            Optional<Screenings> screeningsOptional = screenService.getScreeningById(screeningID);
            Optional<Seats> seatsOptional = seatService.getSeatById(seatID);
            if (screeningsOptional.isPresent() && seatsOptional.isPresent()) {

                Seats seat = seatsOptional.get();
                Integer rowNumber = seat.getRowNumber();
                Integer seatNumber = seat.getSeatNumber();
                String seatType = null;
                Integer vip = seat.getVip();
                if (vip == 1) seatType = "VIP seat";
                else seatType = "Basic seat";
                Screenings screening = screeningsOptional.get();
                Optional<Movies> movieOptional = movieService.getMovieById(screening.getMovieId());
                Optional<Rooms> roomOptional = roomService.getRoomById(screening.getRoomId());
                if (movieOptional.isPresent() && roomOptional.isPresent()) {
                    Movies movie = movieOptional.get();
                    String movieName = movie.getMovieName();
                    String runTime = movie.getRunTime();

                    Rooms room = roomOptional.get();
                    String roomName = room.getRoom_name();

                    ScreeningInfo screeningInfo = new ScreeningInfo(screening, movie, room);

                    content.append("    ").append(movieName).append("   (").append(screening.getStartTime()).append(")").append("\n");
                    content.append("        -  Duration: ").append(runTime).append("\n");
                    content.append("        -  Room and seating: ").append(roomName).append(", ").append(seatType).append("\n");
                    content.append("        -  Row number: ").append(rowNumber).append("\n");
                    content.append("        -  Seat number:").append(seatNumber).append("\n");
                    content.append("        -  Ticket price: ").append(reservation.getSeatCost()).append(" €").append("\n");
                    content.append("\n");

                }

            }


        }
        return content.toString();
    }

    private void sendEmail(String emailAddress, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailAddress);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }
}

