package com.nikita.exchangeratesc.controller;

import com.nikita.exchangeratesc.dto.ExchangeRateResponse;
import com.nikita.exchangeratesc.service.ExchangeCalculatorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController()
@RequestMapping("/api")
@Tag(name = "Exchange Rate Api", description = "Exchange rate endpoints")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeCalculatorService exchangeCalculatorService;

    @GetMapping("/{currency1}/{currency2}")
    public ExchangeRateResponse getExchangeRate(@PathVariable String currency1,
                                                @PathVariable String currency2,
                                                @RequestParam(required = false) Optional<BigDecimal> amount) {

        return exchangeCalculatorService.calcualteExchangeRate(currency1, currency2, amount);
    }



}
