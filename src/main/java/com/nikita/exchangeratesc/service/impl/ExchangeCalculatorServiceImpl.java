package com.nikita.exchangeratesc.service.impl;

import com.nikita.exchangeratesc.dto.ExchangeRateResponse;
import com.nikita.exchangeratesc.exceptions.InvalidAmountException;
import com.nikita.exchangeratesc.exceptions.InvalidCurrencyCodeException;
import com.nikita.exchangeratesc.service.ExchangeCalculatorService;
import com.nikita.exchangeratesc.service.ExchangeRatesRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeCalculatorServiceImpl implements ExchangeCalculatorService {
    private final ExchangeRatesRepositoryService exchangeRatesRepositoryService;
    @Override
    public ExchangeRateResponse calcualteExchangeRate(String fromCurrency, String toCurrency, Optional<BigDecimal> amount) {
        throwIfInvalid(fromCurrency,toCurrency,amount);
        var response = new ExchangeRateResponse();
        ExchangeRateResponse.CurrencyPair pair = new ExchangeRateResponse.CurrencyPair(fromCurrency,toCurrency);
        response.setPair(pair);
        BigDecimal rate = calculateExchangeRate(fromCurrency,toCurrency);
        response.setRate(amount.isEmpty() ? rate : rate.multiply(amount.get()));
        return response;
    }
    private BigDecimal calculateExchangeRate(String fromCurrency, String toCurrency) {
        BigDecimal fromRateEuro = exchangeRatesRepositoryService.getExchangeRate(fromCurrency);
        BigDecimal toRateEuro = exchangeRatesRepositoryService.getExchangeRate(toCurrency);
        return fromRateEuro.divide(toRateEuro,4, RoundingMode.HALF_UP);
    }

    private void throwIfInvalid(String fromCurrency, String toCurrency, Optional<BigDecimal> amount) {
        boolean fromExists = exchangeRatesRepositoryService.hasCurrency(fromCurrency);
        boolean toExists = exchangeRatesRepositoryService.hasCurrency(toCurrency);
        if(!fromExists){
            throw new InvalidCurrencyCodeException(fromCurrency);
        }
        if(!toExists){
            throw new InvalidCurrencyCodeException(toCurrency);
        }
        if(amount.isPresent() && amount.get().compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidAmountException(amount.get().toPlainString());
        }
    }
}
