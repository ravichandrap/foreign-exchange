package com.exchange.service;

import com.exchange.beans.ExchangeRateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExchangeService {

    @Autowired
    ExchangeRepository repository;

    public ExchangeRateEntity save(ExchangeRateEntity entity) {
        return repository.save(entity);
    }

    public List<ExchangeRateEntity> find(LocalDate date) {
        return repository.findByDate(date);
    }

    public List<ExchangeRateEntity> find(LocalDate from, LocalDate to) {
        return repository.findByDateBetween(from, to);
    }
}
