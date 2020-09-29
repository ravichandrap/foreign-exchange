package com.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;

@EnableConfigurationProperties
@SpringBootApplication
public class ForeignExchangeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForeignExchangeApplication.class, args);
    }
}
