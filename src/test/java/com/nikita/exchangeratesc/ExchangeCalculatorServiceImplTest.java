package com.nikita.exchangeratesc;

import com.nikita.exchangeratesc.dto.ExchangeRateResponse;
import com.nikita.exchangeratesc.exceptions.InvalidAmountException;
import com.nikita.exchangeratesc.exceptions.InvalidCurrencyCodeException;
import com.nikita.exchangeratesc.exceptions.ResourceNotFoundException;
import com.nikita.exchangeratesc.service.ExchangeRatesRepositoryService;
import com.nikita.exchangeratesc.service.impl.ExchangeCalculatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeCalculatorServiceImplTest {

    @Mock
    private ExchangeRatesRepositoryService exchangeRatesRepositoryService;

    private ExchangeCalculatorServiceImpl exchangeCalculatorService;

    @BeforeEach
    void setUp() {
        exchangeCalculatorService = new ExchangeCalculatorServiceImpl(exchangeRatesRepositoryService);
    }

    @Test
    void shouldCalculateExchangeRate_WithoutAmount() {
        //Given
        String fromCurrency = "USD";
        String toCurrency = "GBP";

        Optional<BigDecimal> amount = Optional.empty();

        BigDecimal usdToEuroRate = new BigDecimal("0.85");
        BigDecimal gbpToEuroRate = new BigDecimal("1.15");
        BigDecimal expectedRate = gbpToEuroRate.divide(usdToEuroRate, 8, RoundingMode.HALF_UP);

        when(exchangeRatesRepositoryService.hasCurrency(fromCurrency)).thenReturn(true);
        when(exchangeRatesRepositoryService.hasCurrency(toCurrency)).thenReturn(true);
        when(exchangeRatesRepositoryService.getExchangeRate(fromCurrency)).thenReturn(usdToEuroRate);
        when(exchangeRatesRepositoryService.getExchangeRate(toCurrency)).thenReturn(gbpToEuroRate);

        //When
        ExchangeRateResponse response = exchangeCalculatorService.calcualteExchangeRate(fromCurrency, toCurrency, amount);

        //Then
        assertEquals(fromCurrency, response.getPair().fromCurrency());
        assertEquals(toCurrency, response.getPair().toCurrency());
        assertEquals(expectedRate, response.getRate());
        verify(exchangeRatesRepositoryService).updateUsageForPair(fromCurrency,toCurrency);
    }

    @Test
    void shouldCalculateExchangeRate_WithAmount() {
        //Given
        String fromCurrency = "USD";
        String toCurrency = "GBP";
        BigDecimal inputAmount = new BigDecimal("100");
        Optional<BigDecimal> amount = Optional.of(inputAmount);

        BigDecimal usdToEuroRate = new BigDecimal("0.85");
        BigDecimal gbpToEuroRate = new BigDecimal("1.15");
        BigDecimal expectedRate = gbpToEuroRate.divide(usdToEuroRate, 8, RoundingMode.HALF_UP);

        when(exchangeRatesRepositoryService.hasCurrency(fromCurrency)).thenReturn(true);
        when(exchangeRatesRepositoryService.hasCurrency(toCurrency)).thenReturn(true);
        when(exchangeRatesRepositoryService.getExchangeRate(fromCurrency)).thenReturn(usdToEuroRate);
        when(exchangeRatesRepositoryService.getExchangeRate(toCurrency)).thenReturn(gbpToEuroRate);

        //When
        ExchangeRateResponse response = exchangeCalculatorService.calcualteExchangeRate(fromCurrency, toCurrency, amount);

        //Then
        assertEquals(fromCurrency, response.getPair().fromCurrency());
        assertEquals(toCurrency, response.getPair().toCurrency());
        assertEquals(expectedRate.multiply(inputAmount), response.getRate());
        verify(exchangeRatesRepositoryService).updateUsageForPair(fromCurrency,toCurrency);

    }

    @ParameterizedTest
    @ValueSource(strings = {"usd", "US$", "12A", "AB", "USDT", ""})
    @NullAndEmptySource
    void shouldThrowInvalidCurrencyCodeException_WhenFromCurrencyFormatIsInvalid(String invalidCurrency) {
        //Given
        String toCurrency = "EUR";
        Optional<BigDecimal> amount = Optional.empty();
        //When\Then
        InvalidCurrencyCodeException exception = assertThrows(
                InvalidCurrencyCodeException.class,
                () -> exchangeCalculatorService.calcualteExchangeRate(toCurrency, invalidCurrency, amount)
        );

        assertEquals("Currency code doesn't pass validation : " + invalidCurrency, exception.getMessage());
        verify(exchangeRatesRepositoryService, never()).updateUsageForPair(any(), any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"eur", "EU$", "12A", "AB", "USDT", ""})
    @NullAndEmptySource
    void shouldThrowInvalidCurrencyCodeException_WhenToCurrencyFormatIsInvalid(String invalidCurrency) {
        //Given
        String fromCurrency = "USD";
        Optional<BigDecimal> amount = Optional.empty();
        when(exchangeRatesRepositoryService.hasCurrency(fromCurrency)).thenReturn(true);

        //When\Then
        InvalidCurrencyCodeException exception = assertThrows(
                InvalidCurrencyCodeException.class,
                () -> exchangeCalculatorService.calcualteExchangeRate(invalidCurrency, fromCurrency, amount)
        );

        assertEquals("Currency code doesn't pass validation : " + invalidCurrency, exception.getMessage());
        verify(exchangeRatesRepositoryService, never()).updateUsageForPair(any(), any());
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenFromCurrencyDoesNotExist() {
        //Given
        String fromCurrency = "XYZ"; // Valid format but doesn't exist
        String toCurrency = "EUR";
        Optional<BigDecimal> amount = Optional.empty();

        when(exchangeRatesRepositoryService.hasCurrency(fromCurrency)).thenReturn(false);

        //When\Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> exchangeCalculatorService.calcualteExchangeRate(toCurrency, fromCurrency, amount)
        );

        assertEquals("Currency with code: " + fromCurrency + " not found", exception.getMessage());
        verify(exchangeRatesRepositoryService, never()).updateUsageForPair(any(), any());
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenToCurrencyDoesNotExist() {
        //Given
        String fromCurrency = "USD";
        String toCurrency = "XYZ"; // Valid format but doesn't exist
        Optional<BigDecimal> amount = Optional.empty();

        when(exchangeRatesRepositoryService.hasCurrency(fromCurrency)).thenReturn(true);
        when(exchangeRatesRepositoryService.hasCurrency(toCurrency)).thenReturn(false);

        //When\Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> exchangeCalculatorService.calcualteExchangeRate(toCurrency, fromCurrency, amount)
        );

        assertEquals("Currency with code: " + toCurrency + " not found", exception.getMessage());
        verify(exchangeRatesRepositoryService, never()).updateUsageForPair(any(), any());
    }

    @Test
    void shouldThrowException_WhenAmountIsNegative() {
        //Given
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal negativeAmount = new BigDecimal("-10");
        Optional<BigDecimal> amount = Optional.of(negativeAmount);

        when(exchangeRatesRepositoryService.hasCurrency(fromCurrency)).thenReturn(true);
        when(exchangeRatesRepositoryService.hasCurrency(toCurrency)).thenReturn(true);

        //When\Then
        InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> exchangeCalculatorService.calcualteExchangeRate(toCurrency, fromCurrency, amount)
        );

        assertEquals("Amount should be greater than zero", exception.getMessage());
        verify(exchangeRatesRepositoryService, never()).updateUsageForPair(any(), any());
    }

    @Test
    void shouldThrowException_WhenAmountIsZero() {
        //Given
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal zeroAmount = BigDecimal.ZERO;
        Optional<BigDecimal> amount = Optional.of(zeroAmount);

        when(exchangeRatesRepositoryService.hasCurrency(fromCurrency)).thenReturn(true);
        when(exchangeRatesRepositoryService.hasCurrency(toCurrency)).thenReturn(true);

        //When & Then
        InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> exchangeCalculatorService.calcualteExchangeRate(toCurrency, fromCurrency, amount)
        );

        assertEquals("Amount should be greater than zero", exception.getMessage());
        verify(exchangeRatesRepositoryService, never()).updateUsageForPair(any(), any());
    }

    @Test
    void shouldHandleComplexExchangeRateCalculation() {
        //Given
        String fromCurrency = "JPY";
        String toCurrency = "CAD";
        BigDecimal inputAmount = new BigDecimal("10000");
        Optional<BigDecimal> amount = Optional.of(inputAmount);

        BigDecimal jpyToEuroRate = new BigDecimal("0.0075");
        BigDecimal cadToEuroRate = new BigDecimal("0.65");
        BigDecimal expectedRate = cadToEuroRate.divide(jpyToEuroRate, 8, RoundingMode.HALF_UP);

        when(exchangeRatesRepositoryService.hasCurrency(fromCurrency)).thenReturn(true);
        when(exchangeRatesRepositoryService.hasCurrency(toCurrency)).thenReturn(true);
        when(exchangeRatesRepositoryService.getExchangeRate(fromCurrency)).thenReturn(jpyToEuroRate);
        when(exchangeRatesRepositoryService.getExchangeRate(toCurrency)).thenReturn(cadToEuroRate);

        //When
        ExchangeRateResponse response = exchangeCalculatorService.calcualteExchangeRate(fromCurrency,toCurrency, amount);

        //Then
        assertEquals(fromCurrency, response.getPair().fromCurrency());
        assertEquals(toCurrency, response.getPair().toCurrency());
        assertEquals(expectedRate.multiply(inputAmount), response.getRate());

        verify(exchangeRatesRepositoryService).updateUsageForPair(fromCurrency,toCurrency);
    }
}