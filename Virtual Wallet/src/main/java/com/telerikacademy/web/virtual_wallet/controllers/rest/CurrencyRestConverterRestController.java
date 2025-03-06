package com.telerikacademy.web.virtual_wallet.controllers.rest;

import com.telerikacademy.web.virtual_wallet.ExchangeRateFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/currency")
public class CurrencyRestConverterRestController {

    @Autowired
    private ExchangeRateFetcher exchangeRateFetcher;

    @GetMapping("/rates")
    private Map<String, Double> rates() {
        exchangeRateFetcher.updateExchangeRates();
        return exchangeRateFetcher.fetchExchangeRates();
    }

    @GetMapping("/convert")
    private double convert(@RequestParam double amount,
                           @RequestParam String fromCurrency,
                           @RequestParam String toCurrency) {
        return exchangeRateFetcher.convertCurrency(amount,
                fromCurrency.toUpperCase(),
                toCurrency.toUpperCase());
    }
}
