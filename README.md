# Transaction Processing Service

A RESTful Transaction Processing Service built using Java, Spring Boot, Maven, and H2 Database.

## Problem

The service manages customer transactions with the following details:

- Transaction ID
- Customer ID
- Amount
- Currency
- Transaction Type
- Transaction Status

The application supports four operations:

1. Create a transaction
2. Get a transaction by ID
3. Update transaction status
4. Get all transactions for a customer

## Architecture

The application follows a layered architecture:

Controller → Service → Repository → H2 Database

Supporting components include DTOs, Entities, Enums, and Global Exception Handling.

The project is implemented as a modular monolith to keep the solution simple and appropriate for the assignment scope.

## Assumptions

The following assumptions were made:

- Transaction ID is unique.
- A new transaction always starts with `PENDING` status.
- Supported currencies are `INR`, `USD`, and `EUR`.
- Only valid transaction status transitions are allowed.


## Validation Rules

- Transaction ID and Customer ID are required.
- Amount must be greater than zero.
- Supported currencies are INR, USD, and EUR.
- Transaction Type is required.
- Duplicate Transaction IDs are rejected.
- A new transaction is created with PENDING status.

## Status Transition Rules

Allowed transitions:

- PENDING → COMPLETED
- PENDING → FAILED

Invalid transitions, such as COMPLETED → PENDING or FAILED → PENDING, are rejected.

These rules are enforced in the Service layer.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/transactions` | Create a transaction |
| GET | `/api/transactions/{transactionId}` | Get transaction by ID |
| GET | `/api/transactions/customer/{customerId}` | Get transactions for a customer |
| PUT | `/api/transactions/{transactionId}/status` | Update transaction status |

## Error Handling

The application uses centralized exception handling and meaningful HTTP status codes:

- 201 Created – Transaction created successfully
- 200 OK – Successful request
- 400 Bad Request – Invalid input or invalid status transition
- 404 Not Found – Transaction not found
- 409 Conflict – Duplicate Transaction ID

## Testing

Automated tests were implemented using JUnit and Spring Boot Test.

The tests cover:

- Successful transaction creation
- Negative amount validation
- Invalid currency validation
- Duplicate Transaction ID
- Transaction not found
- Valid status transition
- Invalid status transition

Final test result:

Tests run: 8  
Failures: 0  
Errors: 0  
Skipped: 0  

BUILD SUCCESS

## How to Run

Run all tests:

.\mvnw clean test

Run the application:

.\mvnw spring-boot:run

## API Endpoint

The application provides transaction data at:

http://localhost:8080/api/transactions/customer/CUST001

## Known Limitations and Future Improvements

The current implementation does not include authentication, pagination, advanced concurrency handling, idempotency, production database configuration, or API documentation.

With more time, these could be added along with additional tests, logging, monitoring, and production-ready security.

## Design Decisions

DTOs are used to separate the API contract from the database entity. Enums restrict supported values, business rules are handled in the Service layer, and Transaction ID uniqueness is enforced through the persistence model.

The solution was intentionally kept simple, clean, and focused on the assignment requirements.

# AI Usage Disclosure

ChatGPT and AI Cursor was used as an AI coding assistant during this project.

It was used for:

- Understanding the project structure
- Guidance on implementing APIs, DTOs, validation, and exception handling
- Suggestions for transaction status rules
- Assistance with test cases and code review

The final project was run and tested locally. All automated tests passed successfully with 8 tests passing and no failures or errors.
