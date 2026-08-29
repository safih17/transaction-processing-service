# Transaction Processing Service

## About the Project

This project is a simple REST API built using Java and Spring Boot to manage customer transactions.

The application supports four operations:

- Create a transaction
- Get a transaction by Transaction ID
- Update a transaction status
- Get all transactions for a Customer ID

Each transaction contains a Transaction ID, Customer ID, Amount, Currency, Transaction Type, and Transaction Status.

---

## Assumptions and Validation

I made the following decisions for validation:

- Transaction ID must be unique.
- Transaction ID, Customer ID, and Currency cannot be empty.
- Amount must be greater than 0.
- Transaction Type is required.
- Every new transaction is automatically assigned the `PENDING` status.
- Invalid input returns `400 Bad Request`.
- Duplicate Transaction IDs return `409 Conflict`.
- A transaction that does not exist returns `404 Not Found`.

For status updates, any valid status is allowed:

`PENDING`, `COMPLETED`, `FAILED`, `CANCELLED`

I did not add restrictions between status changes because the assignment does not define a specific transaction lifecycle.

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/transactions` | Create a transaction |
| GET | `/api/transactions/{transactionId}` | Get a transaction by ID |
| PUT | `/api/transactions/{transactionId}/status?status=COMPLETED` | Update transaction status |
| GET | `/api/transactions/customer/{customerId}` | Get all transactions for a customer |

Example request:

```json
{
  "transactionId": "TXN001",
  "customerId": "CUST001",
  "amount": 1000,
  "currency": "INR",
  "transactionType": "PAYMENT"
}
```

---

## Testing

Automated tests were written using JUnit and Spring Boot Test with MockMvc.

The tests cover:

1. Successful transaction creation
2. Validation failure for invalid transaction data
3. Duplicate Transaction ID rejection
4. Requesting a transaction that does not exist
5. Invalid transaction status

Run the tests using:

```powershell
.\mvnw.cmd clean test
```

---

## Known Limitations and Improvements

This project uses an H2 in-memory database and does not include authentication or pagination.

With more time, I would add:

- Authentication and authorization
- Pagination for customer transactions
- Custom error responses
- More test cases
- API documentation
- A production database such as PostgreSQL

---

## AI Usage Disclosure

AI was used as a learning and assistance tool to understand the project structure, review code, debug issues, and assist with testing and documentation.

The final code was reviewed and tested before submission.