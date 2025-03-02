package com.nikita.exchangeratesc.controller;

import com.nikita.exchangeratesc.dto.CurrencyResponse;
import com.nikita.exchangeratesc.service.ExchangeRatesRepositoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
@Tag(name = "Currency Info Api", description = "Endpoints for getting currency info")
@RequiredArgsConstructor
public class CurrencyController {
    private final ExchangeRatesRepositoryService exchangeRatesRepositoryService;
    @GetMapping
    public CurrencyResponse getCurrenciesAndTheirUsage(){
        return new CurrencyResponse(exchangeRatesRepositoryService.getUsage());
    }
}
