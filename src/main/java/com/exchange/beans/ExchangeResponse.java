package com.exchange.beans;

import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;

public class ExchangeResponse {
    private Map<String, Map<String, Double>> rates;
    private String base;
    private String startAt;
    private String endAt;

    public ExchangeResponse() {
    }

    public ExchangeResponse(SortedMap<String, Map<String, Double>> rates, String base, String startAt, String endAt) {
        this.rates = rates;
        this.base = base;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getStartAt() {
        return startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public Map<String, Map<String, Double>> getRates() {
        return rates;
    }

    public void setRates(SortedMap<String, Map<String, Double>> rates) {
        this.rates = rates;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeResponse)) return false;
        ExchangeResponse response = (ExchangeResponse) o;
        return Objects.equals(getRates(), response.getRates()) &&
                Objects.equals(getBase(), response.getBase()) &&
                Objects.equals(getStartAt(), response.getStartAt()) &&
                Objects.equals(getEndAt(), response.getEndAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRates(), getBase(), getStartAt(), getEndAt());
    }
}
