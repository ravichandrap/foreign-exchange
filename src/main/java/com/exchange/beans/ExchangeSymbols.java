package com.exchange.beans;

import java.util.Map;
import java.util.Objects;

public class ExchangeSymbols {
    private Map<String, Double> rates;
    private String base;
    private String date;

    public void setBase(String base) {
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeSymbols)) return false;
        ExchangeSymbols that = (ExchangeSymbols) o;
        return Objects.equals(getRates(), that.getRates()) &&
                Objects.equals(getBase(), that.getBase()) &&
                Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRates(), getBase(), getDate());
    }
}
