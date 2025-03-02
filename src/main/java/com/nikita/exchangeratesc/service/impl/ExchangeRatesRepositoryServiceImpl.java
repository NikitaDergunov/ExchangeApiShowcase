package com.nikita.exchangeratesc.service.impl;

import com.nikita.exchangeratesc.exceptions.ResourceNotFoundException;
import com.nikita.exchangeratesc.service.ExchangeRatesRepositoryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class ExchangeRatesRepositoryServiceImpl implements ExchangeRatesRepositoryService {
    private final Map<String, BigDecimal> database = new ConcurrentHashMap<>();
    private final Map<String, BigInteger> usage = new ConcurrentHashMap<>();

    @Override
    public void saveExchangeRates(Map<String, BigDecimal> newRates) {
        // Clear existing rates and add new ones
        database.clear();
        database.putAll(newRates);
        populateUsages(newRates.keySet());
    }

    @Override
    public Map<String, BigDecimal> getAllExchangeRates() {
        // Return an unmodifiable view to prevent external modification
        return Collections.unmodifiableMap(database);
    }

    @Override
    public BigDecimal getExchangeRate(String currencyCode) {
        return Optional.ofNullable(database.get(currencyCode))
                .orElseThrow(()->new ResourceNotFoundException("Currency with code: " + currencyCode + " not found"));
    }

    @Override
    public boolean hasCurrency(String currencyCode) {
        return database.containsKey(currencyCode);
    }
    @Override
    public void updateUsage(String currencyCode) {
        if(!usage.containsKey(currencyCode)) {
            throw new ResourceNotFoundException("Currency with code: " + currencyCode + " not found");
        }
        usage.put(currencyCode, usage.get(currencyCode).add(BigInteger.ONE));
    }

    @Override
    public Set<String> getAllCurrencyCodes() {
        return database.keySet();
    }

    @Override
    public BigInteger getUsage(String currencyCode) {
        return Optional.ofNullable(usage.get(currencyCode))
                .orElseThrow(()->new ResourceNotFoundException("Currency with code: " + currencyCode + " not found"));
    }


    @Override
    public void updateUsageForPair(String currency1, String currency2){
        updateUsage(currency1);
        updateUsage(currency2);
    }
    private void populateUsages(Set<String> keys) {
        for (String key : keys) {
            usage.computeIfAbsent(key, k -> new BigInteger("0"));
        }
    }

}