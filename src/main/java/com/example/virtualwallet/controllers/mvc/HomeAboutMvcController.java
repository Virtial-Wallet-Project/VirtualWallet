package com.example.virtualwallet.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeAboutMvcController {


    private final Map<String, Double> exchangeRates = Map.of(
            "BGN_USD", 0.55,
            "USD_BGN", 1.82,
            "BGN_EUR", 0.51,
            "EUR_BGN", 1.96
    );

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("exchangeRates", exchangeRates);
        return "home-page";
    }

    @GetMapping("/about")
    public String about(){
        return "about-page";
    }

    @GetMapping("/convert")
    public String convertCurrency(@RequestParam double amount,
                                  @RequestParam String fromCurrency,
                                  @RequestParam String toCurrency,
                                  Model model) {
        String key = fromCurrency + "_" + toCurrency;
        double rate = exchangeRates.getOrDefault(key, 1.0);
        double convertedAmount = amount * rate;

        model.addAttribute("convertedAmount", convertedAmount);
        return "conversionResult :: conversionResult";
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
