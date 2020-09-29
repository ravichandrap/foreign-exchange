package com.exchange.http;

import com.exchange.beans.ExchangeRate;
import com.exchange.beans.ExchangeResponse;
import com.exchange.beans.Trend;
import com.exchange.config.ExchangeConfig;
import org.apache.commons.math3.util.Precision;
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
import java.time.chrono.ChronoLocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Component
public class ExchangeHttpService {
    static Logger log = LogManager.getLogger(ExchangeHttpService.class);

    @Autowired
    ExchangeConfig exchangeConfig;

    public ExchangeRate fetchExchangeRates(LocalDate inputDate,
                                           String baseCurrency,
                                           String targetCurrency) throws IOException, InterruptedException {
        log.info("fetchExchangeRates from exchangeratesapi");
        return getExchangeRate(inputDate, requestExchange(inputDate, baseCurrency, targetCurrency));
    }

    private ExchangeRate getExchangeRate(LocalDate inputDate, ExchangeResponse response) {
        double totalPrice = 0.0;
        for( Map<String, Double> date: response.getRates().values()) {
            System.out.println(date.values().toArray()[0]);
            totalPrice += (Double) date.values().toArray()[0];
        }

        double average = totalPrice/5;
        double todayPrice = (Double) response.getRates().get(inputDate.toString()).values().toArray()[0];
        return new ExchangeRate(todayPrice, average, findTrend(response));
    }

    private Trend findTrend(ExchangeResponse response) {
        Trend t = Trend.ascending;

        Map<LocalDate, Map<String, Double>> rates = new TreeMap<>();
        for(Map.Entry<String, Map<String, Double>> d: response.getRates().entrySet()) {
            log.info("key: {}: {}", d.getKey(), d.getValue());
            rates.put(LocalDate.parse(d.getKey()), d.getValue());
        }
        log.info("-----------");
        double []prices = new double[rates.size()];

        int count = 0;
        for(Map.Entry<LocalDate, Map<String, Double>> d: rates.entrySet()) {
            log.info("key: {}: {}", d.getKey(), d.getValue());
            final Object o = d.getValue().values().toArray()[0];
//            (Double) d.getValue().values().toArray()[0];
            prices[count++] = (double)o;
        }
        return getTrend(prices);
    }

    private static Trend getTrend(double[] prices) {
        log.info("pricesL{}", prices);

        for(int i = 0; i< prices.length; i++) {
            System.out.println(prices[i]);
        }

        for(int i = 0; i< prices.length; i++) {
            if(i == prices.length-1) {
                return Trend.constant;
            }
            if(prices[i] == prices[i+1]) {
                continue;
            } else {
                break;
            }
        }

        for(int i = 0; i< prices.length; i++) {
            if(i == prices.length-1) {
                return Trend.descending;
            }
            if(prices[i] < prices[i+1]) {
                break;
            }
        }

        for(int i = 0; i< prices.length; i++) {
            if(i == prices.length-1) {
                return Trend.ascending;
            }

            if(prices[i] > prices[i+1]) {
                break;
            }
        }


        return Trend.undefined;
    }

    private ExchangeResponse requestExchange(LocalDate inputDate, String baseCurrency, String targetCurrency) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder(getUri(inputDate, baseCurrency, targetCurrency))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .GET().build();

        HttpResponse<ExchangeResponse> response = HttpClient.newHttpClient().send(request, new JsonBodyHandler<>(ExchangeResponse.class));
        return response.body();
    }

    private URI getUri(LocalDate inputDate, String baseCurrency, String targetCurrency) {
        return URI.create(exchangeConfig.getExchangeratesapi() +
                "/history?start_at=" + inputDate.minusDays(getMinusDays(inputDate)) +
                "&end_at=" + inputDate +
                "&base=" + baseCurrency +
                "&symbols=" + targetCurrency);
    }

    private long getMinusDays(LocalDate inputDate) {
        return inputDate.getDayOfWeek().getValue() == 5 ? 5 : 6;
    }

    public static void main(String[] args) {
        double []prices = new double[5];
        double d = 1.02;
        for(int i = 0; i< prices.length; i++) {
//            (Double) d.getValue().values().toArray()[0];
//            d = d-0.02;
            prices[i] = d;
        }
        System.out.println(getTrend(prices));
    }
}
