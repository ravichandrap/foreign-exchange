package com.exchange.http;

import com.exchange.beans.ExchangeRate;
import com.exchange.beans.ExchangeResponse;
import com.exchange.beans.ExchangeSymbols;
import com.exchange.config.ExchangeConfig;
import com.exchange.exception.CurrencySymbolsException;
import com.exchange.util.RateTrend;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ExchangeHttpService {
    private final static Logger log = LogManager.getLogger(ExchangeHttpService.class.getName());

    @Autowired
    ExchangeConfig exchangeConfig;

    /**
     * Generate ExchangeRate details: price, target symbol, average, trading etc
     * @param inputDate input LocalDate
     * @param baseCurrency base currency symbol
     * @param targetCurrency target currency symbol
     * @return return ExchangeRate
     * @throws IOException
     * @throws InterruptedException
     */
    public ExchangeRate fetchExchangeRates(LocalDate inputDate,
                                           String baseCurrency,
                                           String targetCurrency) throws IOException, InterruptedException {
        log.info("fetchExchangeRates from exchangeratesapi");
        return getExchangeRate(inputDate, baseCurrency, targetCurrency);
    }

    /**
     * Generate ExchangeRate details: price, target symbol, average, trading etc
     * @param inputDate input LocalDate
     * @param baseCurrency base currency symbol
     * @param targetCurrency target currency symbol
     * @return return ExchangeRate
     * @throws IOException
     */
    public ExchangeRate getExchangeRate(LocalDate inputDate, String baseCurrency,
                                         String targetCurrency) throws IOException, InterruptedException {
        log.info("Calculating exchange rates in getExchangeRate method");
        final ExchangeResponse exchangeResponse = requestExchange(inputDate, baseCurrency, targetCurrency);

        final Map<String, Map<String, Double>> rates = exchangeResponse.getRates();
        final List<Double> targetCurrencyValues = getTargetCurrencyValues(inputDate, targetCurrency, rates);

        //Calculate the average currency price for specific provided date range list of values.
        final Double average = targetCurrencyValues.stream().collect(Collectors.averagingDouble(Double::doubleValue));


        final Double targetPrice = rates.get(inputDate.toString()).get(targetCurrency);
        return new ExchangeRate(targetPrice, average, RateTrend.getTrend(targetCurrencyValues), exchangeResponse.getBase(), targetCurrency, inputDate);
    }

    /**
     * Optimized and return the all list of currency rates as stream object.
     * @param inputDate
     * @param targetCurrency
     * @param rates
     * @return
     */
    public List<Double> getTargetCurrencyValues(LocalDate inputDate, String targetCurrency, Map<String, Map<String, Double>> rates) {
        return rates.entrySet().stream().filter(e -> !inputDate.toString().equalsIgnoreCase(e.getKey()))
                .map(f -> f.getValue().get(targetCurrency)).collect(Collectors.toList());
    }

    /**
     * Fetch the Exchange response raw data.
     * API call to https://api.exchangeratesapi.io
     * @param inputDate requested date
     * @param baseCurrency base currency symbol
     * @param targetCurrency target currency symbol
     * @return raw data of exchange response
     * @throws IOException
     * @throws InterruptedException
     */
    public ExchangeResponse requestExchange(LocalDate inputDate, String baseCurrency, String targetCurrency) throws IOException, InterruptedException {

        final HttpRequest request = HttpRequest.newBuilder(getUri(inputDate, baseCurrency, targetCurrency))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .GET().build();

        final HttpResponse<ExchangeResponse> response = HttpClient.newHttpClient().send(request, new JsonBodyHandler<>(ExchangeResponse.class));
        return response.body();
    }

    /**
     * Return URI with provided input place holders.
     * @param inputDate requested date
     * @param baseCurrency base currency symbol
     * @param targetCurrency target currency symbol
     * @return Prepared URI
     */
    public URI getUri(LocalDate inputDate, String baseCurrency, String targetCurrency) {
        return URI.create(exchangeConfig.getExchangeratesapi() +
                "/history?start_at=" + inputDate.minusDays(7) +
                "&end_at=" + inputDate +
                "&base=" + baseCurrency +
                "&symbols=" + targetCurrency);
    }

    /**
     * Validate the base and target currency symbols with exchangeratesapi
     * @param baseCurrency base currency symbol Ex: USD
     * @param targetCurrency target currency symbol Ex: EUR
     * @throws ConnectException when provides un wrong currency symbols.
     */
    public void validateCurrencySymbols(String baseCurrency, String targetCurrency) throws IOException {
        final Set<String> symbols = getSymbols();

        if (!(symbols.contains(baseCurrency))) {
            throw new CurrencySymbolsException("Invalid currency symbol: " + baseCurrency);
        }

        if (!(symbols.contains(targetCurrency))) {
            throw new CurrencySymbolsException("Invalid currency symbol: " + targetCurrency);
        }
    }

    /**
     *
     * @return Currency symbols
     * @throws ConnectException
     */
    public Set<String> getSymbols() throws IOException {
        HttpResponse<ExchangeSymbols> response = null;
        try {
            final HttpRequest request = HttpRequest.newBuilder(URI.create(exchangeConfig.getExchangeratesapi() + "/latest"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .GET().build();

            response = HttpClient.newHttpClient().send(request, new JsonBodyHandler<>(ExchangeSymbols.class));

        } catch ( InterruptedException|IOException e) {
            throw new IOException("Exchange rates api service is down, please try after some time!");
        }

        return response == null ? Set.of() : response.body().getRates().keySet();
    }

}
