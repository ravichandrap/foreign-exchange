package com.exchange.controller;

import com.exchange.beans.ExchangeRate;
import com.exchange.beans.ExchangeRateEntity;
import com.exchange.beans.ExchangeRateValue;
import com.exchange.beans.Trend;
import com.exchange.service.ExchangeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ExchangeHistoryControllerTest {

    @MockBean
    ExchangeService service;

    @Autowired
    ExchangeHistoryController controller;

    final ExchangeRateEntity entity;
    final String date;
    final String errDate;
    final LocalDate inputDate;
    final ExchangeRate exchangeRate;
    final String baseCurrency;
    final String targetCurrency;
    final double rate;
    final double average;
    final Trend trend;
    final List<ExchangeRateEntity> rateEntities;
    final List<ExchangeRateValue> values;

    ExchangeHistoryControllerTest() {
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
        rateEntities = new ArrayList<>();
        values = new ArrayList<>();

        rateEntities.add(new ExchangeRateEntity(inputDate, rate, average, trend, baseCurrency, targetCurrency));
    }

    @Test
    void daily_test() {
        Mockito.when(service.find(inputDate)).thenReturn(rateEntities);
        final int day = inputDate.getDayOfMonth();
        final int month = inputDate.getMonthValue();
        final int year = inputDate.getYear();
        ResponseEntity<List<ExchangeRateValue>> responseEntity = controller.daily(year, month, day);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ExchangeRateValue> valuesList = responseEntity.getBody();
        assert valuesList != null;
        assertThat(valuesList.size()).isEqualTo(rateEntities.size());
        assertThat(valuesList.get(0)).isNotNull();
        assertThat(valuesList.get(0).getDate()).isEqualTo(rateEntities.get(0).getDate());
        assertThat(valuesList.get(0).getBase()).isEqualTo(rateEntities.get(0).getBase());
    }

    @Test
    void monthly_test() {
        final int month = inputDate.getMonthValue();
        final int year = inputDate.getYear();
        final LocalDate from = LocalDate.of(year, month, 1);
        final LocalDate to = LocalDate.of(year, month, from.getMonth().length(true));

        Mockito.when(service.find(from, to)).thenReturn(rateEntities);

        ResponseEntity<List<ExchangeRateValue>> responseEntity = controller.monthly(year, month);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ExchangeRateValue> valuesList = responseEntity.getBody();
        assert valuesList != null;
        assertThat(valuesList.size()).isEqualTo(rateEntities.size());
        assertThat(valuesList.get(0)).isNotNull();
        assertThat(valuesList.get(0).getDate()).isEqualTo(rateEntities.get(0).getDate());
        assertThat(valuesList.get(0).getBase()).isEqualTo(rateEntities.get(0).getBase());
    }

    @Test
    void convert_entity_to_value_for_monthly_test() {
        List<ExchangeRateValue> valueEntities = controller.convertEntityToValueForMonthly(rateEntities);
        assertThat(valueEntities.size()).isEqualTo(rateEntities.size());
        assertThat(valueEntities.get(0)).isNotNull();
        assertThat(valueEntities.get(0).getBase()).isEqualTo(rateEntities.get(0).getBase());
        assertThat(valueEntities.get(0).getDate()).isEqualTo(rateEntities.get(0).getDate());
    }

    @Test
    void convert_entity_to_value_for_daily_test() {
        List<ExchangeRateValue> valueEntities = controller.convertEntityToValueForDaily(rateEntities);
        assertThat(valueEntities.size()).isEqualTo(rateEntities.size());
        assertThat(valueEntities.get(0).getBase()).isEqualTo(rateEntities.get(0).getBase());
        assertThat(valueEntities.get(0).getDate()).isEqualTo(rateEntities.get(0).getDate());
    }

    @Test
    void copy_properties_test() throws Exception {
        ExchangeRateValue value = controller.copyProperties(entity);
        assertThat(value).isNotNull();
        assertThat(value.getAverage()).isEqualTo(entity.getAverage());
        assertThat(value.getBase()).isEqualTo(entity.getBase());
    }
}