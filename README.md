# Transaction Processing Service

A RESTful Transaction Processing Service built using Java, Spring Boot, Maven, Spring Data JPA, and an H2 in-memory database.

The application manages customer transactions and provides APIs to create transactions, retrieve transaction details, update transaction status, and view transactions for a specific customer.

## Problem Understanding

Each transaction contains the following information:

* Transaction ID
* Customer ID
* Amount
* Currency
* Transaction Type
* Transaction Status

The application supports four core operations:

1. Create a new transaction
2. Retrieve a transaction by ID
3. Update the status of an existing transaction
4. Retrieve all transactions belonging to a customer

## Technology Stack

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 Database
* Maven
* JUnit 5
* Spring Boot Test
* Postman

## Architecture

The application follows a layered architecture:

```text
Controller → Service → Repository → H2 Database
```

Additional components include:

* DTOs for separating the API contract from the database entity
* Entities for database persistence
* Enums for controlled values such as currency, transaction type, and status
* Centralized global exception handling

The application follows a layered architecture, separating responsibilities between the Controller, Service, Repository, and persistence layers., keeping the design simple, clean,

## Assumptions and Validation

The following business rules are applied:

* Transaction ID must be unique.
* Transaction ID and Customer ID are required.
* Amount must be greater than zero.
* Supported currencies are `INR`, `USD`, and `EUR`.
* Transaction Type is required.
* Duplicate Transaction IDs are rejected.
* Every newly created transaction starts with `PENDING` status.

## Status Transition Rules

The following status transitions are allowed:

```text
PENDING → COMPLETED
PENDING → FAILED
```

Once a transaction reaches `COMPLETED` or `FAILED`, it cannot transition back to another status.

These business rules are enforced in the Service layer to maintain valid transaction states.

## API Endpoints

| Method | Endpoint                                   | Description                              |
| ------ | ------------------------------------------ | ---------------------------------------- |
| POST   | `/api/transactions`                        | Create a new transaction                 |
| GET    | `/api/transactions/{transactionId}`        | Retrieve a transaction by ID             |
| GET    | `/api/transactions/customer/{customerId}`  | Retrieve all transactions for a customer |
| PUT    | `/api/transactions/{transactionId}/status` | Update transaction status                |

## Example Request

### Create Transaction

```json
{
  "transactionId": 1,
  "customerId": "CUST001",
  "amount": 500.00,
  "currency": "INR",
  "transactionType": "PAYMENT"
}
```

A successful request creates the transaction with an initial status of `PENDING`.

## Error Handling

The application uses centralized exception handling to provide meaningful HTTP responses.

| Status Code     | Description                                |
| --------------- | ------------------------------------------ |
| 201 Created     | Transaction created successfully           |
| 200 OK          | Request completed successfully             |
| 400 Bad Request | Invalid input or invalid status transition |
| 404 Not Found   | Transaction not found                      |
| 409 Conflict    | Duplicate Transaction ID                   |

## API Testing

The REST API endpoints were manually tested using Postman.

Testing included:

* Creating transactions
* Retrieving transactions by ID
* Retrieving transactions by Customer ID
* Updating transaction status
* Validation scenarios
* Duplicate Transaction ID handling
* Invalid status transitions
* Error handling

## Automated Testing

Automated tests were implemented using JUnit 5 and Spring Boot Test.

The test cases cover:

* Successful transaction creation
* Negative amount validation
* Invalid currency validation
* Duplicate Transaction ID
* Transaction not found
* Valid status transition
* Invalid status transition

### Test Result

```text
Tests run: 8
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

## How to Run

### Run All Tests

```bash
.\mvnw clean test
```

### Run the Application

```bash
.\mvnw spring-boot:run
```

## API Endpoint

The application provides transaction data at:

http://localhost:8080/api/transactions/customer/CUST001

The application runs locally on:

```text
http://localhost:8080
```

## Known Limitations and Future Improvements

The current implementation is focused on the assignment requirements. The following features are not included:

* Authentication and authorization
* Pagination
* Idempotency handling
* Advanced concurrency handling
* Production database configuration
* API documentation

With additional time, the application could be improved by adding:

* Swagger/OpenAPI documentation
* Authentication and authorization
* Logging and monitoring
* Pagination for customer transaction results
* Additional integration and edge-case tests
* Production-ready database configuration

## Design Decisions

DTOs are used to separate the API layer from the persistence model, while enums help restrict supported values and improve type safety.

Business rules, validation, and transaction status transitions are handled in the Service layer. Transaction ID uniqueness is enforced through the persistence model.

The solution was intentionally designed to be simple, maintainable, and focused on the requirements of the engineering challenge.

## AI Usage Disclosure

* Used ChatGPT and Cursor AI for guidance, implementation suggestions, and code review.
* Reviewed and adapted AI suggestions to match the project requirements.
* Corrected issues encountered during development and testing.
* Verified the REST APIs using Postman.
* Ran automated tests using JUnit and Spring Boot Test, with all 8 tests passing successfully.
