package com.nikita.exchangeratesc.dto;


import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

public class BulkCurrencyResponseDto {
    private Set<String> currencies;

    public BulkCurrencyResponseDto(Set<String> currencies) {
        this.currencies = currencies;
    }

    public Set<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Set<String> currencies) {
        this.currencies = currencies;
    }
}
