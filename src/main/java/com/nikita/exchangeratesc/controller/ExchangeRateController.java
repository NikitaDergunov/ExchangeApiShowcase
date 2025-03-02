package com.nikita.exchangeratesc.controller;

import com.nikita.exchangeratesc.dto.ExchangeRateResponse;
import com.nikita.exchangeratesc.service.ExchangeCalculatorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController()
@RequestMapping("/api")
@Tag(name = "Exchange Rate Api", description = "Exchange rate endpoints")
public class ExchangeRateController {
    public ExchangeRateController(ExchangeCalculatorService exchangeCalculatorService) {
        this.exchangeCalculatorService = exchangeCalculatorService;
    }

    private final ExchangeCalculatorService exchangeCalculatorService;

    @GetMapping("/{fromCurrency}/{toCurrency}")
    public ExchangeRateResponse getExchangeRate(@PathVariable String fromCurrency,
                                                @PathVariable String toCurrency,
                                                @RequestParam(required = false) Optional<BigDecimal> amount) {

        return exchangeCalculatorService.calcualteExchangeRate(fromCurrency, toCurrency, amount);
    }



}
