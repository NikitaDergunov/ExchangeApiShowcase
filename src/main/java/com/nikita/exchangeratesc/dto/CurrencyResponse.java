package com.nikita.exchangeratesc.dto;


import java.math.BigInteger;
import java.util.Map;

public class CurrencyResponse {
    public CurrencyResponse(Map<String, BigInteger> currencyToUsage) {
        this.currencyToUsage = currencyToUsage;
    }

    private Map<String, BigInteger> currencyToUsage;

    public Map<String, BigInteger> getCurrencyToUsage() {
        return currencyToUsage;
    }

    public void setCurrencyToUsage(Map<String, BigInteger> currencyToUsage) {
        this.currencyToUsage = currencyToUsage;
    }
}
