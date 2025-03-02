package com.nikita.exchangeratesc.controller;

import com.nikita.exchangeratesc.dto.BulkCurrencyResponseDto;
import com.nikita.exchangeratesc.dto.ErrorResponse;
import com.nikita.exchangeratesc.service.ExchangeRatesRepositoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/currency")
@Tag(name = "Currency Info Api", description = "Endpoints for getting currency info")
public class CurrencyController {
    public CurrencyController(ExchangeRatesRepositoryService exchangeRatesRepositoryService) {
        this.exchangeRatesRepositoryService = exchangeRatesRepositoryService;
    }

    private final ExchangeRatesRepositoryService exchangeRatesRepositoryService;


    @GetMapping
    @Operation(
            summary = "Returns all available currencies",
            description = "This endpoint retrieves a list of all available currency codes supported by the service.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved all available currencies",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BulkCurrencyResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public BulkCurrencyResponseDto getCurrenciesAllCurrencies() {
        return new BulkCurrencyResponseDto(exchangeRatesRepositoryService.getAllCurrencyCodes());
    }
    @GetMapping("/{currency}")
    @Operation(
            summary = "Returns usage of a currency",
            description = "This endpoint retrieves the usage statistics for a specified currency. It returns the number of times the currency is used in exchange requests.",
            parameters = {
                    @Parameter(name = "currency", description = "The currency code for which usage is requested (e.g., USD, EUR)", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved usage statistics for the specified currency",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BigInteger.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Currency not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public BigInteger getCurrencyUsage(@PathVariable("currency") String currency){
        return exchangeRatesRepositoryService.getUsage(currency);
    }
}
