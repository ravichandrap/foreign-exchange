package com.exchange.beans;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "exchange_rate")
public class ExchangeRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    private LocalDate date;
    private double rate;
    private double average;
    private Trend trend;
    private String base;
    private String target;

    public ExchangeRateEntity(Long id, LocalDate date, double rate, double average, Trend trend, String base, String target) {
        this.id = id;
        this.date = date;
        this.rate = rate;
        this.average = average;
        this.trend = trend;
        this.base = base;
        this.target = target;
    }

    public ExchangeRateEntity(LocalDate date, double rate, double average, Trend trend, String base, String target) {
        this.date = date;
        this.rate = rate;
        this.average = average;
        this.trend = trend;
        this.base = base;
        this.target = target;
    }

    public ExchangeRateEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public Trend getTrend() {
        return trend;
    }

    public void setTrend(Trend trend) {
        this.trend = trend;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}


