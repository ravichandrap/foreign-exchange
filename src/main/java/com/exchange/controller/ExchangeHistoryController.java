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

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/exchange-rate/history")
public class ExchangeHistoryController {
    static Logger log = LogManager.getLogger(ExchangeController.class);

    @Autowired
    ExchangeService service;

    //daily
    @GetMapping("/daily/{year}/{month}/{day}")
    public ResponseEntity<List<ExchangeRateValue>> daily(@PathVariable int year,
                                                         @PathVariable int month,
                                                         @PathVariable int day) {
        log.info("Fetching the daily history of exchange rate data:{}/{}/{}", year, month, day);
        List<ExchangeRateEntity> entity = service.find(LocalDate.of(year, month, day));
        return new ResponseEntity<>(convertEntityToValueForDaily(entity), HttpStatus.OK);
    }

    //monthly
    @GetMapping("/monthly/{year}/{month}")
    public ResponseEntity<List<ExchangeRateValue>> monthly(@PathVariable int year, @PathVariable int month) {
        log.info("Fetching the monthly history of exchange rate data:{}/{}", year, month);

        final LocalDate from = LocalDate.of(year, month, 1);
        final LocalDate to = LocalDate.of(year, month, from.getMonth().length(true));
        log.info("from: {}, to:{}", from, to);

        List<ExchangeRateEntity> entity = service.find(from, to);
        return new ResponseEntity<>(convertEntityToValueForMonthly(entity), HttpStatus.OK);
    }

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

    public ExchangeRateValue copyProperties(ExchangeRateEntity e) {
        ExchangeRateValue value = new ExchangeRateValue();
        BeanUtils.copyProperties(e, value);
        return value;
    }
}
