package com.exchange.service;

import com.exchange.beans.ExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface ExchangeRepository extends JpaRepository<ExchangeRateEntity, Long> {
    List<ExchangeRateEntity> findByDate(LocalDate date);

    List<ExchangeRateEntity> findByDateBetween(LocalDate from, LocalDate to);
}
