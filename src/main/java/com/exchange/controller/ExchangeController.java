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
import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/exchange-rate")
public class ExchangeController {
    private static final Logger log = LogManager.getLogger(ExchangeController.class.getName());

    @Autowired
    ExchangeHttpService httpService;

    @Autowired
    ExchangeService service;

    /**
     * Return ExchangeRate: date, target currency symbol, average, trend etc.
     * @param date input date String
     * @param baseCurrency base currency symbol String : Ex. USD
     * @param targetCurrency target currency symbol String Ex. EUR
     * @return ExchangeRate
     * @throws Exception
     */
    @GetMapping("/{date}/{baseCurrency}/{targetCurrency}")
    public ResponseEntity<ExchangeRate> fetchExchangeRates(@PathVariable String date,
                                                           @PathVariable String baseCurrency,
                                                           @PathVariable String targetCurrency) throws DateFormatException, IOException, InterruptedException {
        log.info("Fetch exchange rates date: {}, base currency: {}, target currency: {}",
                date, baseCurrency, targetCurrency);

        httpService.validateCurrencySymbols(baseCurrency, targetCurrency);

        final LocalDate inputDate = validateInputDate(date);
        final ExchangeRate exchangeRate = httpService.fetchExchangeRates(inputDate, baseCurrency, targetCurrency);

        service.save(copyProperties(exchangeRate));
        exchangeRate.add(linkTo(methodOn(ExchangeController.class).fetchExchangeRates(date, baseCurrency, targetCurrency)).withSelfRel());

        return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
    }

    /**
     * Copy the property values of the given source bean ExchangeRate
     * into the target bean ExchangeRateEntity.
     * @param exchangeRate Source bean ExchangeRate
     * @return Return copied target bean ExchangeRateEntity
     */
    public ExchangeRateEntity copyProperties(ExchangeRate exchangeRate) {
        ExchangeRateEntity entity = new ExchangeRateEntity();
        BeanUtils.copyProperties(exchangeRate, entity);
        entity.setDate(exchangeRate.getDate());
        return entity;
    }

    /**
     * Validate input date: Only dates between 2000-01-01 and yesterday are allowed.
     * if success return LocalDate,
     * if fails throw Exception or DateFormatException.
     * @param date input String date
     * @return LocalDate date format
     * @throws DateFormatException DateFormatException throw exception if not valid input date.
     */
    public LocalDate validateInputDate(final String date) throws DateFormatException {
        log.info("Validate input date: {} in validateInputDate method", date);
        final LocalDate inputDate ;
        try {
            inputDate = LocalDate.parse(date);
        } catch (Exception e) {
            throw new DateFormatException("Please enter correct data: "+date);
        }
        final LocalDate yesterday = LocalDate.now().minusDays(1);

        if (inputDate.isBefore(LocalDate.parse("2000-01-01")) || inputDate.isAfter(yesterday)) {
            throw new DateFormatException("Requested date should be between 2000-01-01 to yesterday: " + yesterday);
        } else {
            return LocalDate.parse(date);
        }
    }

}




