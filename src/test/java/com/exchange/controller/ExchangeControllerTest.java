package com.exchange.controller;

import com.exchange.beans.ExchangeRate;
import com.exchange.beans.ExchangeRateEntity;
import com.exchange.beans.Trend;
import com.exchange.exception.DateFormatException;
import com.exchange.http.ExchangeHttpService;
import com.exchange.service.ExchangeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExchangeControllerTest {

    @Autowired
    ExchangeController controller;

    @MockBean
    ExchangeService service;

    @MockBean
    ExchangeHttpService httpService;

    @Autowired
    RestTemplate template;

    @Test
    void contextLoad() {
        assertThat(controller).isNotNull();
    }

    final String date;
    final String errDate;
    final LocalDate inputDate;
    final ExchangeRate exchangeRate;
    final ExchangeRateEntity entity;
    final String baseCurrency;
    final String targetCurrency;
    final double rate;
    final double average;
    final Trend trend;

    ExchangeControllerTest() {
        this.date = "2010-01-01";
        this.errDate = "1999-12-21";
        this.inputDate = LocalDate.parse(this.date);
        this.baseCurrency = "USD";
        this.targetCurrency = "EUR";
        this.rate = 0.778;
        this.average = 0.6767;
        this.trend = Trend.ASCENDING;

        this.exchangeRate = new ExchangeRate(this.rate,
                this.average,
                this.trend,
                this.baseCurrency,
                this.targetCurrency,
                this.inputDate);
        this.entity = new ExchangeRateEntity(inputDate, rate, average, trend, baseCurrency, targetCurrency);
    }

    @Test
    void fetch_exchange_rates_test() throws Exception {
        Mockito.doNothing().when(httpService).validateCurrencySymbols(baseCurrency, targetCurrency);
        Mockito.when(service.save(entity)).thenReturn(entity);

        Mockito.when(httpService.fetchExchangeRates(inputDate, baseCurrency, targetCurrency)).thenReturn(exchangeRate);
        ResponseEntity<ExchangeRate> rateResponseEntity = controller.fetchExchangeRates(date, baseCurrency, targetCurrency);
        assertThat(rateResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ExchangeRate rateBody = rateResponseEntity.getBody();
        assertThat(rateBody).isNotNull();
        assertThat(rateBody.getAverage()).isEqualTo(this.average);
        assertThat(rateBody.getBase()).isEqualTo(this.baseCurrency);
    }

    @Test
    void fetch_exchange_rates_exception_test() throws Exception {
        assertThrows(DateFormatException.class, () -> {
            controller.fetchExchangeRates("1999-12-01", baseCurrency, targetCurrency);
        });
    }

    @Test
    void copy_properties_test() {
        final ExchangeRateEntity entity = controller.copyProperties(exchangeRate);
        assertThat(entity.getAverage()).isEqualTo(exchangeRate.getAverage());
        assertThat(entity.getBase()).isEqualTo(exchangeRate.getBase());
        assertThat(entity.getDate()).isEqualTo(exchangeRate.getDate());
        assertThat(entity.getRate()).isEqualTo(exchangeRate.getRate());
        assertThat(entity.getTarget()).isEqualTo(exchangeRate.getTarget());
        assertThat(entity.getTrend()).isEqualTo(exchangeRate.getTrend());
    }

    @Test
    void validate_input_date_test() throws Exception {
        final LocalDate localDate = controller.validateInputDate(date);
        assertThat(localDate).isEqualTo(inputDate);
    }

    @Test
    void validate_input_date_date_format_exception_test() {
        assertThrows(DateFormatException.class, () -> {
            controller.validateInputDate(errDate);
        });
    }
}