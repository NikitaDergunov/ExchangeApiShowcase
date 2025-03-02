package com.nikita.exchangeratesc.service;

import java.math.BigDecimal;
import java.util.Map;

public interface ECBRatesService {
    Map<String, BigDecimal> getRatesFromECB();
}
