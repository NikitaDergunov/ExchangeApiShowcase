package com.nikita.exchangeratesc.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public interface ExchangeRatesRepositoryService {
    void saveExchangeRates(Map<String, BigDecimal> newRates);
    Map<String, BigDecimal> getAllExchangeRates();
    BigDecimal getExchangeRate(String currencyCode);
     boolean hasCurrency(String currencyCode);

    void updateUsage(String currencyCode);




    Map<String, BigInteger> getUsage();
    void updateUsageForPair(String currency1, String currency2);
}
