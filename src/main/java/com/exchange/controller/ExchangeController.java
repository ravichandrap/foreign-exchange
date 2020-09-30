package com.exchange.controller;

import com.exchange.beans.ExchangeRate;
import com.exchange.beans.ExchangeRateEntity;
import com.exchange.exception.DateFormatException;
import com.exchange.http.ExchangeHttpService;
import com.exchange.service.ExchangeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/exchange-rate")
public class ExchangeController {

    static final Logger log = LogManager.getLogger(ExchangeController.class);

    @Autowired
    ExchangeHttpService httpService;

    @Autowired
    ExchangeService service;

    @GetMapping("/{date}/{baseCurrency}/{targetCurrency}")
    public ResponseEntity<ExchangeRate> fetchExchangeRates(@PathVariable String date,
                                                           @PathVariable String baseCurrency,
                                                           @PathVariable String targetCurrency) throws IOException, InterruptedException {
        log.info("Fetch exchange rates date: {}, base currency: {}, target currency: {}",
                date, baseCurrency, targetCurrency);

        httpService.validateCurrencySymbols(baseCurrency, targetCurrency);

        final LocalDate inputDate = validateInputDate(date);
        final ExchangeRate exchangeRate = httpService.fetchExchangeRates(inputDate, baseCurrency, targetCurrency);

        service.save(copyProperties(exchangeRate));
        exchangeRate.add(linkTo(methodOn(ExchangeController.class).fetchExchangeRates(date, baseCurrency, targetCurrency)).withSelfRel());

        return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
    }

    public ExchangeRateEntity copyProperties(ExchangeRate exchangeRate) {
        ExchangeRateEntity entity = new ExchangeRateEntity();
        BeanUtils.copyProperties(exchangeRate, entity);
        entity.setDate(exchangeRate.getDate());
        return entity;
    }


    public LocalDate validateInputDate(final String date) {
        log.info("Validate input date: {} in validateInputDate method", date);
        final LocalDate inputDate = LocalDate.parse(date);
        final LocalDate yesterday = LocalDate.now().minusDays(1);

        if (inputDate.isBefore(LocalDate.parse("2000-01-01")) || inputDate.isAfter(yesterday)) {
            throw new DateFormatException("Requested date should be between 2000-01-01 to yesterday: " + yesterday);
        } else {
            return LocalDate.parse(date);
        }
    }

}




