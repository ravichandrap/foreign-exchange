package com.exchange.controller;

import com.exchange.beans.ExchangeRate;
import com.exchange.beans.ExchangeResponse;
import com.exchange.config.ExchangeConfig;
import com.exchange.exception.DateFormatException;
import com.exchange.http.ExchangeHttpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

@RestController
@RequestMapping("/api/exchange-rate")
public class ExchangeController {

    static final Logger log = LogManager.getLogger(ExchangeController.class);

    @Autowired
    ExchangeHttpService httpService;

    @GetMapping("/greetings")
    public String greetings() {
        return "result";
    }

    @GetMapping("/{date}/{baseCurrency}/{targetCurrency}")
    public ResponseEntity<ExchangeRate> fetchExchangeRates(@PathVariable String date,
                                                     @PathVariable String baseCurrency,
                                                     @PathVariable String targetCurrency) throws IOException, InterruptedException {
        log.info("Fetch exchange rates date: {}, base currency: {}, target currency: {}",
                date, baseCurrency, targetCurrency);

        final LocalDate inputDate = validateInputDate(date);
        ExchangeRate exchangeRate = httpService.fetchExchangeRates(inputDate, baseCurrency, targetCurrency);
        return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
    }

    private LocalDate validateInputDate(final String date) {
        log.info("Validate input date: {} in validateInputDate method", date);
        final LocalDate inputDate = LocalDate.parse(date);
        final LocalDate yesterday = LocalDate.now().minusDays(1);

        if (inputDate.isBefore(LocalDate.parse("2000-01-01"))
                || inputDate.isAfter(yesterday)
                || inputDate.getDayOfWeek().getValue() == 6
                || inputDate.getDayOfWeek().getValue() == 7) {
            throw new DateFormatException("Can not request for weekends and requested date should be between 2000-01-01 to yesterday: " + yesterday);
        } else {
            return LocalDate.parse(date);
        }
    }
}
