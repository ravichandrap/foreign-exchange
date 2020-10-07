package com.exchange.service;

import com.exchange.beans.ExchangeRateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExchangeService {

    @Autowired
    ExchangeRepository repository;

    /**
     * Save exchange rate entity.
     * @param entity ExchangeRateEntity
     * @return ExchangeRateEntity
     */
    public ExchangeRateEntity save(ExchangeRateEntity entity) {
        return repository.save(entity);
    }

    /**
     * Fetch all users exchange rate entity list for input date.
     * @param date input date.
     * @return list of entity ExchangeRateEntity.
     */
    public List<ExchangeRateEntity> find(LocalDate date) {
        return repository.findByDate(date);
    }

    /**
     * Fetch all users exchange rate entity list for input date range.
     * @param from input from date.
     * @param to input to date.
     * @return list of entity ExchangeRateEntity.
     */
    public List<ExchangeRateEntity> find(LocalDate from, LocalDate to) {
        return repository.findByDateBetween(from, to);
    }
}
