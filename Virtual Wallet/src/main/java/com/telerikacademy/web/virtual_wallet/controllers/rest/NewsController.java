package com.telerikacademy.web.virtual_wallet.controllers.rest;

import com.telerikacademy.web.virtual_wallet.FinancialNewsFetcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final FinancialNewsFetcher financialNewsFetcher;

    public NewsController(FinancialNewsFetcher financialNewsFetcher) {
        this.financialNewsFetcher = financialNewsFetcher;
    }

    @GetMapping
    public List<String> getLatestNews() {
        return financialNewsFetcher.fetchLatestNews();
    }
}
