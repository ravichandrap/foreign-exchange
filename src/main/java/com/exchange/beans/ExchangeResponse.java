package com.exchange.beans;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ExchangeResponse {
    private Map<String, Map<String, Double>> rates;
    //    private Rates rates;
    private String base;
    private String start_at;
    private String end_at;

    public ExchangeResponse() {
    }

    public ExchangeResponse(Map<String, Map<String, Double>> rates, String base, String start_at, String end_at) {
        this.rates = rates;
        this.base = base;
        this.start_at = start_at;
        this.end_at = end_at;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public String getStart_at() {
        return start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public Map<String, Map<String, Double>> getRates() {
        return rates;
    }

    public void setRates(Map<String, Map<String, Double>> rates) {
        this.rates = rates;
    }
}
//
//class Rates {
//    private String date;
//    private List<Currency> currencies;
//}
//
//class Currency {
//    private String symbol;
//    private String value;
//
//}
