package com.exchange.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("url")
public class ExchangeConfig {
    private String exchangeratesapi;

    public void setExchangeratesapi(String exchangeratesapi) {
        this.exchangeratesapi = exchangeratesapi;
    }

    public String getExchangeratesapi() {
        return exchangeratesapi;
    }
}
