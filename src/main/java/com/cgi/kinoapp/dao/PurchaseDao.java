package com.cgi.kinoapp.dao;

import com.cgi.kinoapp.model.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseDao extends JpaRepository<Purchases, Integer> {


}
