package com.nikita.exchangeratesc.dto;

import java.util.Set;

//Represents an api response containing all currencies
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
