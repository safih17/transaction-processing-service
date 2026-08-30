package com.example.transactionstarter.controller;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.TransactionResponse;
import com.example.transactionstarter.dto.UpdateTransactionStatusRequest;
import com.example.transactionstarter.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Create a new transaction
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        TransactionResponse createdTransaction =
                transactionService.createTransaction(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTransaction);
    }

    // Get transaction by transaction ID
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable String transactionId) {

        TransactionResponse transaction =
                transactionService.getTransactionById(transactionId);

        return ResponseEntity.ok(transaction);
    }

    // Get all transactions for a customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TransactionResponse>>
    getTransactionsByCustomerId(
            @PathVariable String customerId) {

        List<TransactionResponse> transactions =
                transactionService
                        .getTransactionsByCustomerId(customerId);

        return ResponseEntity.ok(transactions);
    }

    // Update transaction status
    @PutMapping("/{transactionId}/status")
    public ResponseEntity<TransactionResponse> updateTransactionStatus(
            @PathVariable String transactionId,
            @Valid @RequestBody UpdateTransactionStatusRequest request) {

        TransactionResponse updatedTransaction =
                transactionService.updateTransactionStatus(
                        transactionId,
                        request.getStatus());

        return ResponseEntity.ok(updatedTransaction);
    }
}