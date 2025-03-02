package com.nikita.exchangeratesc.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ExchangeRateResponse {
    CurrencyPair pair;
    BigDecimal rate;
    public record CurrencyPair(String fromCurrency, String toCurrency){};
}
