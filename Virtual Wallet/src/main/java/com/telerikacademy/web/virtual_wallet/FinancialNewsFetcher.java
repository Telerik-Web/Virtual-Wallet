package com.telerikacademy.web.virtual_wallet;

import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class FinancialNewsFetcher {

    private static final String API_URL = "https://newsapi.org/v2/top-headlines?category=business&apiKey=2e8ba681c5084eeea0915922daf70587";

    public List<String> fetchLatestNews() {

        WebClient webClient = WebClient.builder().baseUrl(API_URL).build();
        String response = webClient.get().retrieve().bodyToMono(String.class).block();

        List<String> newsList = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray articles = jsonResponse.getJSONArray("articles");

            for (int i = 0; i < Math.min(11, articles.length()); i++) {
                JSONObject article = articles.getJSONObject(i);
                String title = article.getString("title");
                newsList.add(title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }
}
