package com.telerikacademy.web.virtual_wallet.controllers.mvc;

import com.telerikacademy.web.virtual_wallet.ExchangeRateFetcher;
import com.telerikacademy.web.virtual_wallet.models.CurrencyDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/currency")
public class CurrencyConverterMvcController {

    private final ExchangeRateFetcher exchangeRateFetcher;

    public CurrencyConverterMvcController(ExchangeRateFetcher exchangeRateFetcher) {
        this.exchangeRateFetcher = exchangeRateFetcher;
    }

    @GetMapping("/convert")
    public String showCurrencyForm(Model model) {
        if (!model.containsAttribute("currency")) {
            model.addAttribute("currency", new CurrencyDTO());
        }
        return "indexNotLogged";
    }

    @PostMapping("/convert")
    public String convertCurrency(@Valid @ModelAttribute("currency") CurrencyDTO currencyDTO,
                                  BindingResult errors,
                                  Model model) {
        try {
            Double.parseDouble(currencyDTO.getAmount());
        } catch (NumberFormatException e) {
            errors.rejectValue("amount", "error.amount");
            return "indexNotLogged";
        }

        double convertedAmount;

        try {
            convertedAmount = exchangeRateFetcher.convertCurrency(Double.parseDouble(currencyDTO.getAmount()),
                    currencyDTO.getFrom().toUpperCase(),
                    currencyDTO.getTo().toUpperCase());
        } catch (IllegalArgumentException e) {
            errors.rejectValue("currencyDTO.getFrom()", "error.amount");
            return "indexNotLogged";
        }

        model.addAttribute("convertedAmount", convertedAmount);
        return "indexNotLogged";
    }
}
