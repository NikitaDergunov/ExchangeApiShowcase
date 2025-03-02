package com.nikita.exchangeratesc.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.Map;
@Data
public class CurrencyResponse {
    public CurrencyResponse(Map<String, BigInteger> currencyToUsage) {
        this.currencyToUsage = currencyToUsage;
    }

    Map<String, BigInteger> currencyToUsage;
}
