package com.exchange.controller;

import com.exchange.beans.ExchangeRateEntity;
import com.exchange.beans.ExchangeRateValue;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/exchange-rate/history")
public class ExchangeHistoryController {
    private static final Logger log = LogManager.getLogger(ExchangeController.class.getName());

    @Autowired
    ExchangeService service;

    /**
     * Return all daily user requested exchange rate details.
     * @param year input year number
     * @param month input month number
     * @param day input day of month number
     * @return return list of exchange rate details for user requested input year and month.
     */
    @GetMapping("/daily/{year}/{month}/{day}")
    public ResponseEntity<List<ExchangeRateValue>> daily(@PathVariable int year,
                                                         @PathVariable int month,
                                                         @PathVariable int day) {
        log.info("Fetching the daily history of exchange rate data:{}/{}/{}", year, month, day);
        List<ExchangeRateEntity> entity = service.find(LocalDate.of(year, month, day));
        return new ResponseEntity<>(convertEntityToValueForDaily(entity), HttpStatus.OK);
    }

    /**
     * Return all monthly user requested exchange rate details.
     * @param year input year number.
     * @param month input month number.
     * @return return list of exchange rate details for user requested input year and month.
     */
    @GetMapping("/monthly/{year}/{month}")
    public ResponseEntity<List<ExchangeRateValue>> monthly(@PathVariable int year, @PathVariable int month) {
        log.info("Fetching the monthly history of exchange rate data:{}/{}", year, month);

        final LocalDate from = LocalDate.of(year, month, 1);
        final LocalDate to = LocalDate.of(year, month, from.getMonth().length(true));
        log.info("from: {}, to:{}", from, to);

        List<ExchangeRateEntity> entity = service.find(from, to);
        return new ResponseEntity<>(convertEntityToValueForMonthly(entity), HttpStatus.OK);
    }

    /**
     * Convert all list of ExchangeRateEntity into list of ExchangeRateValue value objects
     * and also generate self and monthly links.
     * @param entity list of ExchangeRateEntity.
     * @return list of ExchangeRateValue.
     */
    public List<ExchangeRateValue> convertEntityToValueForMonthly(List<ExchangeRateEntity> entity) {
        List<ExchangeRateValue> values = new ArrayList<>(entity.size());

        for (ExchangeRateEntity e : entity) {
            ExchangeRateValue value = copyProperties(e);
            LocalDate date = LocalDate.parse(value.getDate().toString());
            value.add(linkTo(methodOn(ExchangeHistoryController.class).monthly(date.getYear(), date.getMonthValue())).withSelfRel(),
                    linkTo(methodOn(ExchangeHistoryController.class).daily(date.getYear(), date.getMonthValue(), date.getDayOfMonth())).withRel("daily"));
            values.add(value);
        }
        return values;
    }

    /**
     * Convert all list of ExchangeRateEntity into list of ExchangeRateValue value objects
     * and also generate self and daily links.
     * @param entity list of ExchangeRateEntity.
     * @return list of ExchangeRateValue.
     */
    public List<ExchangeRateValue> convertEntityToValueForDaily(List<ExchangeRateEntity> entity) {
        List<ExchangeRateValue> values = new ArrayList<>(entity.size());

        for (ExchangeRateEntity e : entity) {
            ExchangeRateValue value = copyProperties(e);
            LocalDate date = LocalDate.parse(value.getDate().toString());
            value.add(linkTo(methodOn(ExchangeHistoryController.class).daily(date.getYear(), date.getMonthValue(), date.getDayOfMonth())).withSelfRel(),
                    linkTo(methodOn(ExchangeHistoryController.class).monthly(date.getYear(), date.getMonthValue())).withRel("monthly"));
            values.add(value);
        }
        return values;
    }

    /**
     * Convert entity object ExchangeRateEntity into value object ExchangeRateValue
     * @param entity entity object ExchangeRateEntity
     * @return value object ExchangeRateValue
     */
    public ExchangeRateValue copyProperties(ExchangeRateEntity entity) {
        ExchangeRateValue value = new ExchangeRateValue();
        BeanUtils.copyProperties(entity, value);
        return value;
    }
}
