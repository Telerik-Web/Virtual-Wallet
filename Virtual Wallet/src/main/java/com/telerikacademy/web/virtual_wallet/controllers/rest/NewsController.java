package com.telerikacademy.web.virtual_wallet.controllers.rest;

import com.telerikacademy.web.virtual_wallet.FinancialNewsFetcher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@Tag(name = "News Controller", description = "APIs for financial news")
public class NewsController {

    private final FinancialNewsFetcher financialNewsFetcher;

    public NewsController(FinancialNewsFetcher financialNewsFetcher) {
        this.financialNewsFetcher = financialNewsFetcher;
    }

    @Operation(summary = "Get financial news", description = "Gets latest financial news available")
    @GetMapping
    public List<String> getLatestNews() {
        return financialNewsFetcher.fetchLatestNews();
    }
}
