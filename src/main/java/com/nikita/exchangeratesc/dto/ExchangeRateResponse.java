package com.nikita.exchangeratesc.dto;


import java.math.BigDecimal;

//Represents the response of the calculation
public class ExchangeRateResponse {
    private CurrencyPair pair;
    private BigDecimal rate;
    public record CurrencyPair(String fromCurrency, String toCurrency){}

    public CurrencyPair getPair() {
        return pair;
    }

    public void setPair(CurrencyPair pair) {
        this.pair = pair;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
