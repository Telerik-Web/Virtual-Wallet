package com.telerikacademy.web.virtual_wallet;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Service
public class FinancialNewsFetcher {

    private static final String API_URL = "https://newsapi.org/v2/top-headlines?category=business&apiKey=2e8ba681c5084eeea0915922daf70587";

    public List<String> fetchLatestNews() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(API_URL, String.class);

        List<String> newsList = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray articles = jsonResponse.getJSONArray("articles");

            for (int i = 0; i < Math.min(5, articles.length()); i++) {
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
