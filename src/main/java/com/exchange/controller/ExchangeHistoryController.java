package com.exchange.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange-rate/history")
public class ExchangeHistoryController {
    static Logger log = LogManager.getLogger(ExchangeController.class);

    //daily
    @GetMapping("/daily/{year}/{month}/{day}")
    public ResponseEntity<Object> daily(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        log.info("Fetching the daily history of exchange rate data:{}/{}/{}", year, month, day);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //monthly
    @GetMapping("/monthly/{year}/{month}")
    public ResponseEntity<Object> monthly(@PathVariable int year, @PathVariable int month) {
        log.info("Fetching the monthly history of exchange rate data:{}/{}", year, month);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
