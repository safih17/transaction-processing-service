package com.example.transactionstarter.controller;

import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.service.TransactionService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Transaction> createTransaction(
            @Valid @RequestBody Transaction transaction) {

        Transaction createdTransaction =
                transactionService.createTransaction(transaction);

        return ResponseEntity.ok(createdTransaction);
    }

    // Get transaction by transaction ID
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(
            @PathVariable String transactionId) {

        Transaction transaction =
                transactionService.getTransactionById(transactionId);

        return ResponseEntity.ok(transaction);
    }

    // Get all transactions for a customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactionsByCustomerId(
            @PathVariable String customerId) {

        List<Transaction> transactions =
                transactionService.getTransactionsByCustomerId(customerId);

        return ResponseEntity.ok(transactions);
    }

    // Update transaction status
    @PutMapping("/{transactionId}/status")
    public ResponseEntity<Transaction> updateTransactionStatus(
            @PathVariable String transactionId,
            @RequestParam String status) {

        Transaction updatedTransaction =
                transactionService.updateTransactionStatus(
                        transactionId, status);

        return ResponseEntity.ok(updatedTransaction);
    }
}