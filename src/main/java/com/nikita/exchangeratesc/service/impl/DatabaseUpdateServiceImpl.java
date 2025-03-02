package com.nikita.exchangeratesc.service.impl;

import com.nikita.exchangeratesc.service.DatabaseUpdateService;
import com.nikita.exchangeratesc.service.ECBRatesService;
import com.nikita.exchangeratesc.service.ExchangeRatesRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DatabaseUpdateServiceImpl implements DatabaseUpdateService {
    private final ECBRatesService ecbRatesService;
    private final ExchangeRatesRepositoryService exchangeRatesRepositoryService;

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "${foo.update}",zone = "CET")
    @Override
    public void runImport(){
        var ratesFromECB = ecbRatesService.getRatesFromECB();
        exchangeRatesRepositoryService.saveExchangeRates(ratesFromECB);
    }


}
