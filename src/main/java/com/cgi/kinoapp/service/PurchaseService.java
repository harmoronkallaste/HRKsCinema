package com.cgi.kinoapp.service;

import com.cgi.kinoapp.dao.PurchaseDao;
import com.cgi.kinoapp.dao.ReservationDao;
import com.cgi.kinoapp.model.Purchases;
import com.cgi.kinoapp.model.Reservations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseDao purchaseDao;

    @Autowired
    private ReservationDao reservationDao;

    public Purchases addPurchase(BigDecimal totalCost) {
        Purchases purchase = new Purchases();
        purchase.setTotalCost(totalCost);
        purchase.setConfirmedAt(new Timestamp(System.currentTimeMillis()));

        return purchaseDao.save(purchase);
    }

    public Optional<Purchases> getPurchaseById(Integer purchaseId) {
        return purchaseDao.findById(purchaseId);
    }

    public List<Reservations> getReservationsByPurchaseId(Integer purchaseId) {
        return reservationDao.findAllByPurchaseId(purchaseId);
    }

}
