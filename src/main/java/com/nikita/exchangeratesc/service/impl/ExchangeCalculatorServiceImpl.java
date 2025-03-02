package com.nikita.exchangeratesc.service.impl;

import com.nikita.exchangeratesc.dto.ExchangeRateResponse;
import com.nikita.exchangeratesc.exceptions.InvalidAmountException;
import com.nikita.exchangeratesc.exceptions.InvalidCurrencyCodeException;
import com.nikita.exchangeratesc.exceptions.ResourceNotFoundException;
import com.nikita.exchangeratesc.service.ExchangeCalculatorService;
import com.nikita.exchangeratesc.service.ExchangeRatesRepositoryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class ExchangeCalculatorServiceImpl implements ExchangeCalculatorService {
    private final ExchangeRatesRepositoryService exchangeRatesRepositoryService;

    public ExchangeCalculatorServiceImpl(ExchangeRatesRepositoryService exchangeRatesRepositoryService) {
        this.exchangeRatesRepositoryService = exchangeRatesRepositoryService;
    }

    @Override
    public ExchangeRateResponse calcualteExchangeRate(String fromCurrency,String toCurrency, Optional<BigDecimal> amount) {
        throwIfInvalid(toCurrency,fromCurrency,amount);
        updateUsages(fromCurrency,toCurrency);
        BigDecimal rate = calculateExchangeRate(toCurrency,fromCurrency);
        return buildResponse(fromCurrency, toCurrency, amount, rate);
    }

    private static ExchangeRateResponse buildResponse(String fromCurrency, String toCurrency, Optional<BigDecimal> amount, BigDecimal rate) {
        var response = new ExchangeRateResponse();
        ExchangeRateResponse.CurrencyPair pair = new ExchangeRateResponse.CurrencyPair(fromCurrency, toCurrency);
        response.setPair(pair);
        response.setRate(amount.map(rate::multiply).orElse(rate));
        return response;
    }

    private void updateUsages(String fromCurrency, String toCurrency) {
        exchangeRatesRepositoryService.updateUsageForPair(fromCurrency,toCurrency);
    }

    private BigDecimal calculateExchangeRate(String fromCurrency, String toCurrency) {
        BigDecimal fromRateEuro = exchangeRatesRepositoryService.getExchangeRate(fromCurrency);
        BigDecimal toRateEuro = exchangeRatesRepositoryService.getExchangeRate(toCurrency);
        return fromRateEuro.divide(toRateEuro,8, RoundingMode.HALF_UP);
    }

    private void throwIfInvalid(String fromCurrency, String toCurrency, Optional<BigDecimal> amount) {
        validateCurrency(fromCurrency);
        validateCurrency(toCurrency);
        validateAmount(amount);
    }
    private void validateCurrency(String currency) {
        if(!isValidCurrencyCodeRegex(currency)) {
            throw new InvalidCurrencyCodeException("Currency code doesn't pass validation : " + currency);
        }
        if (!exists(currency)) {
            throw new ResourceNotFoundException("Currency with code: " + currency + " not found");
        }
    }
    private boolean exists(String currency) {
        return exchangeRatesRepositoryService.hasCurrency(currency);
    }
    private static boolean isValidCurrencyCodeRegex(String code) {
        return code != null && code.matches("^[A-Z]{3}$");
    }

    private static void validateAmount(Optional<BigDecimal> amount) {
        if(amount.isPresent() && amount.get().compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidAmountException("Amount should be greater than zero");
        }
    }
}
