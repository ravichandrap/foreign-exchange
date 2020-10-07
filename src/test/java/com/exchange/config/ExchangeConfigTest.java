package com.exchange.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExchangeConfigTest {

    @Autowired
    ExchangeConfig exchangeConfig;

    @Test
    public void get_property_test() {
        final String apiProp = exchangeConfig.getExchangeratesapi();
        assertEquals(apiProp, "https://api.exchangeratesapi.io");
    }
}