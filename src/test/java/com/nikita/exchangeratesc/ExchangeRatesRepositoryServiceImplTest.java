package com.nikita.exchangeratesc;
import com.nikita.exchangeratesc.exceptions.ResourceNotFoundException;
import com.nikita.exchangeratesc.service.ExchangeRatesRepositoryService;
import com.nikita.exchangeratesc.service.*;
import com.nikita.exchangeratesc.service.impl.ExchangeRatesRepositoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRatesRepositoryServiceImplTest {
    private ExchangeRatesRepositoryServiceImpl exchangeRatesRepositoryService;

    @BeforeEach
    void setUp() {
        exchangeRatesRepositoryService = new ExchangeRatesRepositoryServiceImpl();
    }
    @Test
    void testSaveExchangeRates() {
        Map<String, BigDecimal> newRates = Map.of("USD", BigDecimal.valueOf(1.1), "EUR", BigDecimal.valueOf(0.9));

        exchangeRatesRepositoryService.saveExchangeRates(newRates);

        assertEquals(2, exchangeRatesRepositoryService.getAllExchangeRates().size());
        assertEquals(BigDecimal.valueOf(1.1), exchangeRatesRepositoryService.getExchangeRate("USD"));
        assertEquals(BigDecimal.valueOf(0.9), exchangeRatesRepositoryService.getExchangeRate("EUR"));
    }

    @Test
    void testGetExchangeRate_ShouldThrowException_WhenCurrencyNotFound() {
        exchangeRatesRepositoryService.saveExchangeRates(Map.of("USD", BigDecimal.valueOf(1.1)));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            exchangeRatesRepositoryService.getExchangeRate("GBP");
        });

        assertEquals("Currency with code: GBP not found", exception.getMessage());
    }

    @Test
    void testGetExchangeRate_ShouldReturnRate_WhenCurrencyExists() {
        exchangeRatesRepositoryService.saveExchangeRates(Map.of("USD", BigDecimal.valueOf(1.1)));

        BigDecimal rate = exchangeRatesRepositoryService.getExchangeRate("USD");

        assertEquals(BigDecimal.valueOf(1.1), rate);
    }

    @Test
    void testHasCurrency() {
        exchangeRatesRepositoryService.saveExchangeRates(Map.of("USD", BigDecimal.valueOf(1.1)));

        assertTrue(exchangeRatesRepositoryService.hasCurrency("USD"));
        assertFalse(exchangeRatesRepositoryService.hasCurrency("EUR"));
    }

    @Test
    void testUpdateUsage_ShouldThrowException_WhenCurrencyNotFound() {
        exchangeRatesRepositoryService.saveExchangeRates(Map.of("USD", BigDecimal.valueOf(1.1)));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            exchangeRatesRepositoryService.updateUsage("GBP");
        });

        assertEquals("Currency with code: GBP not found", exception.getMessage());
    }

    @Test
    void testUpdateUsage_ShouldIncrementUsage_WhenCurrencyExists() {
        exchangeRatesRepositoryService.saveExchangeRates(Map.of("USD", BigDecimal.valueOf(1.1)));

        exchangeRatesRepositoryService.updateUsage("USD");
        BigInteger usage = exchangeRatesRepositoryService.getUsage("USD");

        assertEquals(BigInteger.ONE, usage);
    }

    @Test
    void testGetAllCurrencyCodes() {
        exchangeRatesRepositoryService.saveExchangeRates(Map.of("USD", BigDecimal.valueOf(1.1), "EUR", BigDecimal.valueOf(0.9)));

        Set<String> currencyCodes = exchangeRatesRepositoryService.getAllCurrencyCodes();

        assertTrue(currencyCodes.contains("USD"));
        assertTrue(currencyCodes.contains("EUR"));
    }

    @Test
    void testUpdateUsageForPair() {
        exchangeRatesRepositoryService.saveExchangeRates(Map.of("USD", BigDecimal.valueOf(1.1), "EUR", BigDecimal.valueOf(0.9)));

        exchangeRatesRepositoryService.updateUsageForPair("USD", "EUR");

        assertEquals(BigInteger.ONE, exchangeRatesRepositoryService.getUsage("USD"));
        assertEquals(BigInteger.ONE, exchangeRatesRepositoryService.getUsage("EUR"));
    }
}
