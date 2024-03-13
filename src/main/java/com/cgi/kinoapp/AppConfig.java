package com.cgi.kinoapp;

import com.cgi.kinoapp.dao.EmailSender;
import com.cgi.kinoapp.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class AppConfig {
    @Bean
    public EmailSender emailSender(PurchaseService purchaseService, JavaMailSender javaMailSender, ScreenService screenService, SeatService seatService, MovieService movieService) {
        return new EmailService(purchaseService, javaMailSender, screenService, seatService, movieService);
    }
}