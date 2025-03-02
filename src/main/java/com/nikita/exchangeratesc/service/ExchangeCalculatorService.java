package com.nikita.exchangeratesc.service;

import com.nikita.exchangeratesc.dto.ExchangeRateResponse;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeCalculatorService {
    ExchangeRateResponse calcualteExchangeRate(String fromCurrency,
                                               String toCurrency,
                                               Optional<BigDecimal> amount);
}
