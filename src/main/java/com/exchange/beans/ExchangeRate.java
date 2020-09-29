package com.exchange.beans;

public class ExchangeRate {
    private double exchangeRate;
    private double average;
    private Trend trend;

    public ExchangeRate(double exchangeRate, double average, Trend trend) {
        this.exchangeRate = exchangeRate;
        this.average = average;
        this.trend = trend;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
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
}


