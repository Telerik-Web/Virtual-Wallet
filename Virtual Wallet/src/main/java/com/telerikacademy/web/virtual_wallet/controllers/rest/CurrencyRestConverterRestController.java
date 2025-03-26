package com.telerikacademy.web.virtual_wallet.controllers.rest;

import com.telerikacademy.web.virtual_wallet.ExchangeRateFetcher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/currency")
@Tag(name = "Currency Converter Controller", description = "APIs for currency rates and conversion")
public class CurrencyRestConverterRestController {

    @Autowired
    private ExchangeRateFetcher exchangeRateFetcher;

    @Operation(summary = "Get exchange rates", description = "Fetches current exchange rates of all currencies")
    @GetMapping("/rates")
    private Map<String, Double> rates() {
        exchangeRateFetcher.updateExchangeRates();
        return exchangeRateFetcher.fetchExchangeRates();
    }

    @Operation(summary = "Convert currency", description = "Converts a selected currency to another one passed as a parameter")
    @GetMapping("/convert")
    private double convert(@RequestParam double amount,
                           @RequestParam String fromCurrency,
                           @RequestParam String toCurrency) {
        return exchangeRateFetcher.convertCurrency(amount,
                fromCurrency.toUpperCase(),
                toCurrency.toUpperCase());
    }
}
