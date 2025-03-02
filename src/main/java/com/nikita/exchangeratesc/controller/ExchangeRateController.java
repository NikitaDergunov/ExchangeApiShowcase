package com.nikita.exchangeratesc.controller;

import com.nikita.exchangeratesc.dto.ErrorResponse;
import com.nikita.exchangeratesc.dto.ExchangeRateResponse;
import com.nikita.exchangeratesc.service.ExchangeCalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController()
@RequestMapping("/api")
@Tag(name = "Exchange Rate Api", description = "Exchange rate endpoints")
public class ExchangeRateController {
    public ExchangeRateController(ExchangeCalculatorService exchangeCalculatorService) {
        this.exchangeCalculatorService = exchangeCalculatorService;
    }

    private final ExchangeCalculatorService exchangeCalculatorService;

    @GetMapping("/{fromCurrency}/{toCurrency}")
    @Operation(
            summary = "Returns the exchange rate from one currency to another",
            description = "This endpoint retrieves the exchange rate for a specified currency pair, and optionally multiplies it by an amount if provided.",
            parameters = {
                    @Parameter(name = "fromCurrency", description = "The currency code of the source currency (e.g., USD)", required = true),
                    @Parameter(name = "toCurrency", description = "The currency code of the target currency (e.g., EUR)", required = true),
                    @Parameter(name = "amount", description = "An optional amount to be converted. If not provided, the exchange rate will be returned as is.", required = false)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the exchange rate",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExchangeRateResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Invalid input for currencies or amount",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Currency pair not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ExchangeRateResponse getExchangeRate(@PathVariable String fromCurrency,
                                                @PathVariable String toCurrency,
                                                @RequestParam(required = false) Optional<BigDecimal> amount) {

        return exchangeCalculatorService.calcualteExchangeRate(fromCurrency, toCurrency, amount);
    }



}
