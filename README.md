# Transaction Processing Service

A RESTful Transaction Processing Service built using Java, Spring Boot, Maven, Spring Data JPA, and an H2 in-memory database.

The application manages customer transactions and provides APIs to create transactions, retrieve transaction details, update transaction status, and retrieve transactions for a specific customer.

## Problem Understanding

Each transaction contains the following information:

- Transaction ID
- Customer ID
- Amount
- Currency
- Transaction Type
- Transaction Status

The application supports four core operations:

1. Create a new transaction
2. Retrieve a transaction by ID
3. Update the status of an existing transaction
4. Retrieve all transactions belonging to a customer

## Technology Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- Maven
- JUnit 5
- Spring Boot Test
- Postman

## Architecture

The application follows a layered architecture:

```text
Controller → Service → Repository → H2 Database
```

Additional components include:

- **DTOs** for separating the API contract from the database entity
- **Entities** for database persistence
- **Enums** for controlled values such as currency, transaction type, and transaction status
- **Global Exception Handling** for consistent error responses

The layered architecture separates responsibilities across the Controller, Service, Repository, and persistence layers. This keeps the design simple, maintainable, and appropriate for the assignment requirements.

## Assumptions and Validation

The following business rules are applied:

- Transaction ID must be unique.
- Transaction ID and Customer ID are required.
- Amount must be greater than zero.
- Supported currencies are `INR`, `USD`, and `EUR`.
- Transaction Type is required.
- Duplicate Transaction IDs are rejected.
- Every newly created transaction starts with `PENDING` status.

Input validation also restricts Transaction ID and Customer ID length and ensures that transaction amounts follow the configured numeric limits.

## Status Transition Rules

The following status transitions are allowed:

```text
PENDING → COMPLETED
PENDING → FAILED
```

Once a transaction reaches `COMPLETED` or `FAILED`, it cannot transition back to another status.

These business rules are enforced in the Service layer to maintain valid transaction states.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/transactions` | Create a new transaction |
| GET | `/api/transactions/{transactionId}` | Retrieve a transaction by ID |
| GET | `/api/transactions/customer/{customerId}` | Retrieve all transactions for a customer |
| PUT | `/api/transactions/{transactionId}/status` | Update transaction status |

## Example Request

### Create Transaction

**POST** `/api/transactions`

```json
{
  "transactionId": "TXN001",
  "customerId": "CUST001",
  "amount": 500.00,
  "currency": "INR",
  "transactionType": "PAYMENT"
}
```

A successful request creates the transaction with an initial status of `PENDING`.

### Example Response

```json
{
  "transactionId": "TXN001",
  "customerId": "CUST001",
  "amount": 500.00,
  "currency": "INR",
  "transactionType": "PAYMENT",
  "transactionStatus": "PENDING"
}
```

## PowerShell API Examples

The following commands can be used in PowerShell to test the API after starting the application.

### Create a Transaction

```powershell
$body = @{
    transactionId = "TXN001"
    customerId = "CUST001"
    amount = 500.00
    currency = "INR"
    transactionType = "PAYMENT"
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/transactions" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### Get a Transaction by ID

```powershell
Invoke-RestMethod `
    -Uri "http://localhost:8080/api/transactions/TXN001" `
    -Method GET
```

### Get Transactions by Customer ID

```powershell
Invoke-RestMethod `
    -Uri "http://localhost:8080/api/transactions/customer/CUST001" `
    -Method GET
```

### Update Transaction Status

```powershell
Invoke-RestMethod `
    -Uri "http://localhost:8080/api/transactions/TXN001/status?status=COMPLETED" `
    -Method PUT
```

### Example Error Test — Duplicate Transaction

```powershell
$body = @{
    transactionId = "TXN001"
    customerId = "CUST001"
    amount = 500.00
    currency = "INR"
    transactionType = "PAYMENT"
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/transactions" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

This should return a `409 Conflict` response because `TXN001` already exists.

## Error Handling

The application uses centralized exception handling to provide consistent and meaningful HTTP responses.

| Status Code | Description |
|-------------|-------------|
| 201 Created | Transaction created successfully |
| 200 OK | Request completed successfully |
| 400 Bad Request | Invalid input, unsupported enum value, malformed request, or invalid status transition |
| 404 Not Found | Transaction not found |
| 409 Conflict | Duplicate Transaction ID |
| 500 Internal Server Error | Unexpected server error |

## API Testing

The REST API endpoints were manually tested using Postman.

Testing included:

- Creating transactions
- Retrieving transactions by ID
- Retrieving transactions by Customer ID
- Updating transaction status
- Validation scenarios
- Duplicate Transaction ID handling
- Invalid currency handling
- Invalid status transitions
- Error handling

## Integration Testing

Integration tests were implemented using JUnit 5, Spring Boot Test, and the H2 in-memory database.

The tests verify application behavior across multiple layers of the application rather than testing isolated methods with mocks.

The test cases cover:

- Successful transaction creation
- Negative amount validation
- Invalid currency validation
- Duplicate Transaction ID
- Transaction not found
- Valid status transition
- Invalid status transition
- Application context loading

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

The application runs locally at:

```text
http://localhost:8080
```

## Example API Endpoint

Retrieve transactions for a specific customer:

```text
GET /api/transactions/customer/CUST001
```

## Known Limitations and Future Improvements

The current implementation is focused on the requirements of the engineering challenge.

The following features are not included:

- Authentication and authorization
- Pagination
- Idempotency handling
- Advanced concurrency handling
- Production database configuration
- API documentation

With additional time, the application could be improved by adding:

- Swagger/OpenAPI documentation
- Authentication and authorization
- Logging and monitoring
- Pagination for customer transaction results
- Additional integration and edge-case tests
- Advanced concurrency and idempotency handling
- Production-ready database configuration

## Design Decisions

- **DTOs** are used to separate the API layer from the persistence model.
- **Enums** restrict supported values and improve type safety.
- **BigDecimal** is used for transaction amounts to avoid floating-point precision issues.
- Business rules, validation, and transaction status transitions are handled in the **Service layer**.
- Duplicate Transaction IDs are checked before creating a new transaction and rejected with a `409 Conflict` response under normal request handling.
- Centralized exception handling provides a consistent error response structure for validation, malformed requests, application errors, and unexpected failures.
- The application uses an **H2 in-memory database**, allowing the project to run without external database setup.
- `spring.jpa.open-in-view` is set to `false` to avoid keeping the persistence context open during the web response lifecycle and to maintain a clearer separation between the web and persistence layers.

## AI Usage Disclosure

ChatGPT and Cursor AI were used as coding assistants during the development of this project.

They were used for:

- Understanding the project structure
- Implementation guidance
- Suggestions for APIs, DTOs, validation, and exception handling
- Code review and debugging assistance
- Suggestions for test cases

All generated or suggested code was reviewed and adapted to meet the project requirements.

The REST APIs were manually verified using Postman, and automated integration tests were run successfully with all 8 tests passing.