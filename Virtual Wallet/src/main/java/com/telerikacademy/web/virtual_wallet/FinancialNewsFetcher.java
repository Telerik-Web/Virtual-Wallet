//package com.telerikacademy.web.virtual_wallet;
//import org.apache.hc.client5.http.classic.methods.ClassicHttpRequests;
//import org.apache.hc.client5.http.classic.methods.HttpGet;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class FinancialNewsFetcher {
//    private static final String API_KEY = "2e8ba681c5084eeea0915922daf70587";
//    private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines?category=business&apiKey=" + API_KEY;
//
//    public static void main(String[] args) {
//        fetchFinancialNews();
//    }
//
//    public static List<Map<String, String>> fetchFinancialNews() {
//        List<Map<String, String>> articlesList = new ArrayList<>();
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpGet request = new HttpGet(NEWS_API_URL);
//            try (CloseableHttpResponse response = httpClient.execute(request)) {
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());
//                JsonNode articles = jsonResponse.get("articles");
//
//                for (JsonNode article : articles) {
//                    Map<String, String> articleData = new HashMap<>();
//                    articleData.put("title", article.get("title").asText());
//                    articleData.put("url", article.get("url").asText());
//                    articlesList.add(articleData);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return articlesList;
//    }
//
//}
