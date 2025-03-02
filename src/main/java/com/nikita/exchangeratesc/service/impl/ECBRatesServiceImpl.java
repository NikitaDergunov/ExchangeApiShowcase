package com.nikita.exchangeratesc.service.impl;

import com.nikita.exchangeratesc.dto.ecb.ECBCurrencyCubeDto;
import com.nikita.exchangeratesc.dto.ecb.ECBEnvelopeDto;
import com.nikita.exchangeratesc.service.ECBRatesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

@Service
public class ECBRatesServiceImpl implements ECBRatesService {
    @Value("${rates.ecb-url}")
    private String ECB_RATES_URL;

    private final RestTemplate restTemplate;

    public ECBRatesServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, BigDecimal> getRatesFromECB() {
        Map<String, BigDecimal> response = new ConcurrentHashMap<>();

        try {
            // Get XML from ECB
            ECBEnvelopeDto envelope = restTemplate.getForObject(ECB_RATES_URL, ECBEnvelopeDto.class);

            // Add EUR/EUR rate (1.0) as base currency
            response.put("EUR", BigDecimal.ONE);

            // Extract rates from XML structure
            List<ECBCurrencyCubeDto> currencyCubes = envelope.getCube().getTimeCube().getECBCurrencyCubeDtos();

            // Populate map with currency codes and rates
            for (ECBCurrencyCubeDto cube : currencyCubes) {
                response.put(cube.getCurrency(), cube.getRate());
            }

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse ECB exchange rates", e);
        }
    }
}