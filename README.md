# Exchange Rate Service

## Project Overview

A Spring Boot microservice for retrieving and managing currency exchange rates, demonstrating real-world API development
and integration with external financial data sources.

## Features

- Retrieve ECB (European Central Bank) reference exchange rates
- Support for currency pair conversions
- Currency request tracking and statistics
- Flexible exchange rate calculations

## Technical Stack

- Java
- Spring Boot
- Spring Web
- JUnit
- Mockito

## Key Functionality

- Fetch exchange rates for various currency pairs
- Convert amounts between different currencies
- Track and report currency request frequencies
- Daily updated data retrieval from ECB reference rates

## API Endpoints

- `/{baseCurrency}/{targetCurrency}`: Get exchange rate for a specific currency pair with an optional amount
  parameter
- `/currencies`: List supported currencies with request statistics
- `/currencies/currency`: Get currency usage statistics

## Data Source

Exchange rates are fetched daily from the European Central Bank's daily published reference rates, ensuring up-to-date
financial information.

## Development Highlights

- In-memory caching of exchange rates
- Comprehensive unit and integration testing
- Clean, modular architecture
- Efficient error handling

## Running the Application

### Prerequisites

- Java 21
- Docker

### Build and Run

- See Dockerfile

### Testing

```bash
mvn test
```

## Acknowledgements

Project developed as a technical assessment, demonstrating software engineering skills in API development and financial
data integration.


