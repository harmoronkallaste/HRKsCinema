package com.cgi.kinoapp.dao;

public interface EmailSender {
    void sendPurchaseConfirmationEmail(Integer purchaseId, String emailAddress);
}
