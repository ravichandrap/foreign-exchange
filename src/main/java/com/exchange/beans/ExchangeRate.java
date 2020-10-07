package com.exchange.beans;


import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.Objects;

public class ExchangeRate extends RepresentationModel<ExchangeRate> {
    private LocalDate date;
    private double rate;
    private double average;
    private Trend trend;
    private String base;
    private String target;

    public ExchangeRate(double exchangeRate, double average, Trend trend, String base, String target, LocalDate date) {
        this.rate = exchangeRate;
        this.average = average;
        this.trend = trend;
        this.base = base;
        this.target = target;
        this.date = date;
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
        if (!(o instanceof ExchangeRate)) return false;
        if (!super.equals(o)) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Double.compare(that.getRate(), getRate()) == 0 &&
                Double.compare(that.getAverage(), getAverage()) == 0 &&
                Objects.equals(getDate(), that.getDate()) &&
                getTrend() == that.getTrend() &&
                Objects.equals(getBase(), that.getBase()) &&
                Objects.equals(getTarget(), that.getTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDate(), getRate(), getAverage(), getTrend(), getBase(), getTarget());
    }
}


