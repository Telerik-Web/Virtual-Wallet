package com.telerikacademy.web.virtual_wallet;

import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRateFetcher {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/a5c192e177f538a2e3353e2a/latest/USD";

    private Map<String, Double> exchangeRates = new HashMap<>();

    public Map<String, Double> fetchExchangeRates() {
        WebClient webClient = WebClient.builder().baseUrl(API_URL).build();

        // Fetch JSON response
        String response = webClient.get().retrieve().bodyToMono(String.class).block();

        Map<String, Double> exchangeRates = new HashMap<>();

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject rates = jsonResponse.getJSONObject("conversion_rates");

            for (String currency : rates.keySet()) {
                exchangeRates.put(currency, rates.getDouble(currency));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exchangeRates;
    }

    public void updateExchangeRates() {
        WebClient webClient = WebClient.builder().baseUrl(API_URL).build();
        String response = webClient.get().retrieve().bodyToMono(String.class).block();

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject rates = jsonResponse.getJSONObject("conversion_rates");

            exchangeRates.clear();
            for (String currency : rates.keySet()) {
                exchangeRates.put(currency, rates.getDouble(currency));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        if (exchangeRates.isEmpty()) {
            updateExchangeRates();
        }

        if (!exchangeRates.containsKey(fromCurrency) || !exchangeRates.containsKey(toCurrency)) {
            throw new IllegalArgumentException("Invalid currency code provided.");
        }

        double rateFromUSD = exchangeRates.get(fromCurrency);
        double rateToUSD = exchangeRates.get(toCurrency);

        // Convert to USD first, then to the target currency
        return (amount / rateFromUSD) * rateToUSD;
    }

}


//a5c192e177f538a2e3353e2a