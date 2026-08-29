package com.example.transactionstarter.service;

import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.enums.TransactionStatus;
import com.example.transactionstarter.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Create a new transaction
    public Transaction createTransaction(Transaction transaction) {

        if (transactionRepository.existsById(transaction.getTransactionId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Transaction already exists"
            );
        }

        // Always set initial status as PENDING
        transaction.setTransactionStatus(TransactionStatus.PENDING);

        return transactionRepository.save(transaction);
    }

    // Get transaction by ID
    public Transaction getTransactionById(String transactionId) {

        return transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Transaction not found"
                        )
                );
    }

    // Get all transactions for a customer
    public List<Transaction> getTransactionsByCustomerId(String customerId) {

        return transactionRepository.findByCustomerId(customerId);
    }

    // Update transaction status
    public Transaction updateTransactionStatus(
            String transactionId,
            String status) {

        Transaction transaction = getTransactionById(transactionId);

        try {
            TransactionStatus newStatus =
                    TransactionStatus.valueOf(status.toUpperCase());

            transaction.setTransactionStatus(newStatus);

            return transactionRepository.save(transaction);

        } catch (IllegalArgumentException e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid transaction status"
            );
        }
    }
}