package com.nikita.exchangeratesc;

import com.nikita.exchangeratesc.dto.ecb.ECBCubeDto;
import com.nikita.exchangeratesc.dto.ecb.ECBCurrencyCubeDto;
import com.nikita.exchangeratesc.dto.ecb.ECBEnvelopeDto;
import com.nikita.exchangeratesc.dto.ecb.ECBTimeCubeDto;
import com.nikita.exchangeratesc.service.impl.ECBRatesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ECBRatesServiceImplTest {
    private static final String TEST_ECB_URL = "test_url";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ECBRatesServiceImpl ecbRatesService;

    @BeforeEach
    void setUp() {
        //Set the private static field value
        ReflectionTestUtils.setField(ECBRatesServiceImpl.class, "ECB_RATES_URL", TEST_ECB_URL);
    }

    @Test
    void testGetRatesFromECB_Success() {
        //Given
        ECBEnvelopeDto mockEnvelope = createMockEnvelope();


        when(restTemplate.getForObject(eq(TEST_ECB_URL), eq(ECBEnvelopeDto.class)))
                .thenReturn(mockEnvelope);

        //When
        Map<String, BigDecimal> result = ecbRatesService.getRatesFromECB();

        //Then
        assertEquals(4, result.size()); // EUR + 3 test currencies
        assertEquals(BigDecimal.ONE, result.get("EUR"));
        assertEquals(new BigDecimal("1.1234"), result.get("USD"));
        assertEquals(new BigDecimal("0.8765"), result.get("GBP"));
        assertEquals(new BigDecimal("120.45"), result.get("JPY"));
    }

    @Test
    void testGetRatesFromECB_ThrowsException_WhenRestTemplateFailsWithException() {

        when(restTemplate.getForObject(anyString(), eq(ECBEnvelopeDto.class)))
                .thenThrow(new RuntimeException("Network error"));

        //When
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ecbRatesService.getRatesFromECB();
        });

        assertEquals("Failed to fetch or parse ECB exchange rates", exception.getMessage());
    }

    private ECBEnvelopeDto createMockEnvelope() {
        //Create test currency cubes
        ECBCurrencyCubeDto usdCube = new ECBCurrencyCubeDto();
        usdCube.setCurrency("USD");
        usdCube.setRate(new BigDecimal("1.1234"));

        ECBCurrencyCubeDto gbpCube = new ECBCurrencyCubeDto();
        gbpCube.setCurrency("GBP");
        gbpCube.setRate(new BigDecimal("0.8765"));

        ECBCurrencyCubeDto jpyCube = new ECBCurrencyCubeDto();
        jpyCube.setCurrency("JPY");
        jpyCube.setRate(new BigDecimal("120.45"));

        List<ECBCurrencyCubeDto> currencyCubes = Arrays.asList(usdCube, gbpCube, jpyCube);

        //Create TimeCube
        ECBTimeCubeDto timeCube = new ECBTimeCubeDto();
        timeCube.setTime("2025-03-02");
        timeCube.setECBCurrencyCubeDtos(currencyCubes);

        //Create Cube
        ECBCubeDto cube = new ECBCubeDto();
        cube.setTimeCube(timeCube);

        //create envelope
        ECBEnvelopeDto envelope = new ECBEnvelopeDto();
        envelope.setCube(cube);

        return envelope;
    }
}
