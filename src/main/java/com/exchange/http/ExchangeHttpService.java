package com.exchange.http;

import com.exchange.beans.ExchangeRate;
import com.exchange.beans.ExchangeResponse;
import com.exchange.beans.ExchangeSymbols;
import com.exchange.beans.Trend;
import com.exchange.config.ExchangeConfig;
import com.exchange.exception.CurrencySymbolsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Component
public class ExchangeHttpService {
    static Logger log = LogManager.getLogger(ExchangeHttpService.class);


    @Autowired
    ExchangeConfig exchangeConfig;

    public ExchangeRate fetchExchangeRates(LocalDate inputDate,
                                           String baseCurrency,
                                           String targetCurrency) throws IOException, InterruptedException {
        log.info("fetchExchangeRates from exchangeratesapi");
        return getExchangeRate(inputDate, baseCurrency, targetCurrency);
    }

    private ExchangeRate getExchangeRate(LocalDate inputDate, String baseCurrency,
                                         String targetCurrency) throws IOException, InterruptedException {
        log.info("Calculating exchange rates in getExchangeRate method");
        final ExchangeResponse exchangeResponse = requestExchange(inputDate, baseCurrency, targetCurrency);

        final Map<String, Map<String, Double>> rates = exchangeResponse.getRates();

        double totalPrice2 = 0.0;
        int count = 0;

        for (Map.Entry<String, Map<String, Double>> m : rates.entrySet()) {
            if (!m.getKey().equalsIgnoreCase(inputDate.toString())) {
                for (Map.Entry<String, Double> innerMap : m.getValue().entrySet()) {
                    totalPrice2 += innerMap.getValue();
                    count++;
                }
            }
        }

        final double average = totalPrice2 / count;

//        final double targetPrice = (Double) rates.get(inputDate.toString()).values().toArray()[0];
        final Double targetPrice = rates.get(inputDate.toString()).get(targetCurrency);
        return new ExchangeRate(targetPrice, average, findTrend(rates), exchangeResponse.getBase(), targetCurrency, inputDate);
    }

    public ExchangeResponse requestExchange(LocalDate inputDate, String baseCurrency, String targetCurrency) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder(getUri(inputDate, baseCurrency, targetCurrency))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .GET().build();

        final HttpResponse<ExchangeResponse> response = HttpClient.newHttpClient().send(request, new JsonBodyHandler<>(ExchangeResponse.class));
        return response.body();
    }

    public URI getUri(LocalDate inputDate, String baseCurrency, String targetCurrency) {
        return URI.create(exchangeConfig.getExchangeratesapi() +
                "/history?start_at=" + inputDate.minusDays(7) +
                "&end_at=" + inputDate +
                "&base=" + baseCurrency +
                "&symbols=" + targetCurrency);
    }

    public Trend findTrend(Map<String, Map<String, Double>> responseRates) {
        log.info("Finding trend in findTrend method");
        final Trend t = Trend.ascending;

        final Map<LocalDate, Map<String, Double>> rates = new TreeMap<>();

        for (Map.Entry<String, Map<String, Double>> d : responseRates.entrySet()) {
            rates.put(LocalDate.parse(d.getKey()), d.getValue());
        }
        double[] prices = new double[rates.size()];

        int count = 0;
        for (Map.Entry<LocalDate, Map<String, Double>> d : rates.entrySet()) {
            final Object o = d.getValue().values().toArray()[0];
//            (Double) d.getValue().values().toArray()[0];
            prices[count++] = (double) o;
        }
        return getTrend(prices);
    }

    public static Trend getTrend(double[] prices) {
        log.info("pricesL{}", prices);

        for (int i = 0; i < prices.length; i++) {
            log.info(prices[i]);
        }

        //constant
        for (int i = 0; i < prices.length; i++) {
            if (i == prices.length - 1) {
                return Trend.constant;
            }
            if (prices[i] == prices[i + 1]) {
                continue;
            } else {
                break;
            }
        }

        //descending
        for (int i = 0; i < prices.length; i++) {
            if (i == prices.length - 1) {
                return Trend.descending;
            }
            if (prices[i] < prices[i + 1]) {
                break;
            }
        }

        for (int i = 0; i < prices.length; i++) {
            if (i == prices.length - 1) {
                return Trend.ascending;
            }

            if (prices[i] > prices[i + 1]) {
                break;
            }
        }

        return Trend.undefined;
    }

    public void validateCurrencySymbols(String baseCurrency, String targetCurrency) throws IOException, InterruptedException {
        final Set<String> symbols = getSymbols();

        if (!(symbols.contains(baseCurrency))) {
            throw new CurrencySymbolsException("Invalid currency symbol: " + baseCurrency);
        }

        if (!(symbols.contains(targetCurrency))) {
            throw new CurrencySymbolsException("Invalid currency symbol: " + targetCurrency);
        }
    }

    public Set<String> getSymbols() throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.exchangeratesapi.io/latest"))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .GET().build();

        HttpResponse<ExchangeSymbols> response = HttpClient.newHttpClient().send(request, new JsonBodyHandler<>(ExchangeSymbols.class));
        return response.body().getRates().keySet();
    }

    public static void main(String[] args) {
        double[] prices = new double[5];
        double d = 1.02;
        //            (Double) d.getValue().values().toArray()[0];
        //            d = d-0.02;
        Arrays.fill(prices, d);
        System.out.println(getTrend(prices));
    }
}
