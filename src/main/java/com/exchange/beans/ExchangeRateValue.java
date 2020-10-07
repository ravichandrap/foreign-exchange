package com.exchange.beans;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.Objects;

public class ExchangeRateValue extends RepresentationModel<ExchangeRateValue> {

    private Long id;
    private LocalDate date;
    private double rate;
    private double average;
    private Trend trend;
    private String base;
    private String target;

    public ExchangeRateValue(Long id, LocalDate date, double rate, double average, Trend trend, String base, String target) {
        this.id = id;
        this.date = date;
        this.rate = rate;
        this.average = average;
        this.trend = trend;
        this.base = base;
        this.target = target;
    }

    public ExchangeRateValue(LocalDate date, double rate, double average, Trend trend, String base, String target) {
        this.date = date;
        this.rate = rate;
        this.average = average;
        this.trend = trend;
        this.base = base;
        this.target = target;
    }

    public ExchangeRateValue() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRateValue entity = (ExchangeRateValue) o;
        return Double.compare(entity.rate, rate) == 0 &&
                Double.compare(entity.average, average) == 0 &&
                Objects.equals(id, entity.id) &&
                Objects.equals(date, entity.date) &&
                trend == entity.trend &&
                Objects.equals(base, entity.base) &&
                Objects.equals(target, entity.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, rate, average, trend, base, target);
    }
}


