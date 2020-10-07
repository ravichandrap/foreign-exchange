package com.exchange.service;

import com.exchange.beans.ExchangeRate;
import com.exchange.beans.ExchangeRateEntity;
import com.exchange.beans.Trend;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ExchangeServiceTest {

    @Autowired
    ExchangeService service;

    @MockBean
    ExchangeRepository repository;

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

    ExchangeServiceTest() {
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
    public void save_test() {
        Mockito.when(repository.save(entity)).thenReturn(entity);

        ExchangeRateEntity responseEntity = service.save(entity);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getBase()).isEqualTo(entity.getBase());
        assertThat(responseEntity.getDate()).isEqualTo(entity.getDate());
    }

    @Test
    public void find_test() {
        Mockito.when(repository.findByDate(inputDate)).thenReturn(Collections.singletonList(entity));

        List<ExchangeRateEntity> resEntities = service.find(inputDate);
        assert resEntities != null;
        assertThat(resEntities.size()).isEqualTo(1);
        ExchangeRateEntity entity = resEntities.get(0);
        assertThat(entity.getBase()).isEqualTo(this.entity.getBase());
        assertThat(entity.getDate()).isEqualTo(this.entity.getDate());
    }

    @Test
    public void find_by_date_between_test() {
        final int month = inputDate.getMonthValue();
        final int year = inputDate.getYear();
        final LocalDate from = LocalDate.of(year, month, 1);
        final LocalDate to = LocalDate.of(year, month, from.getMonth().length(true));

        Mockito.when(repository.findByDateBetween(from, to)).thenReturn(Collections.singletonList(entity));
        List<ExchangeRateEntity> entities = service.find(from, to);
        assert entities != null;
        assertThat(entities.size()).isEqualTo(1);
        ExchangeRateEntity entity = entities.get(0);
        assertThat(entity.getBase()).isEqualTo(this.entity.getBase());
        assertThat(entity.getDate()).isEqualTo(this.entity.getDate());
    }

}