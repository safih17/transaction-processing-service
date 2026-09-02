package com.example.transactionstarter.service;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.TransactionResponse;
import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.enums.TransactionStatus;
import com.example.transactionstarter.repository.TransactionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Create a new transaction
    public TransactionResponse createTransaction(
            CreateTransactionRequest request) {

        // Check if transaction ID already exists
        if (transactionRepository.existsById(request.getTransactionId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Transaction already exists"
            );
        }

        // Convert DTO to Entity
        Transaction transaction = new Transaction();

        transaction.setTransactionId(request.getTransactionId());
        transaction.setCustomerId(request.getCustomerId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setTransactionType(request.getTransactionType());

        // Always set initial status as PENDING
        transaction.setTransactionStatus(TransactionStatus.PENDING);

        try {
            Transaction savedTransaction =
                    transactionRepository.saveAndFlush(transaction);

            return convertToResponse(savedTransaction);

        } catch (DataIntegrityViolationException exception) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Transaction already exists"
            );
        }
    }

    // Get transaction by ID
    public TransactionResponse getTransactionById(
            String transactionId) {

        Transaction transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Transaction not found"
                        )
                );

        return convertToResponse(transaction);
    }

    // Get all transactions for a customer
    public List<TransactionResponse> getTransactionsByCustomerId(
            String customerId) {

        List<Transaction> transactions =
                transactionRepository.findByCustomerId(customerId);

        return transactions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Update transaction status
    public TransactionResponse updateTransactionStatus(
            String transactionId,
            String status) {

        Transaction transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Transaction not found"
                        )
                );

        try {
            TransactionStatus newStatus =
                    TransactionStatus.valueOf(status.toUpperCase());

            // Only PENDING transactions can change status
            if (transaction.getTransactionStatus()
                    != TransactionStatus.PENDING) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Transaction status cannot be changed once it is COMPLETED or FAILED"
                );
            }

            // PENDING can only change to COMPLETED or FAILED
            if (newStatus != TransactionStatus.COMPLETED &&
                    newStatus != TransactionStatus.FAILED) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "PENDING transaction can only be changed to COMPLETED or FAILED"
                );
            }

            transaction.setTransactionStatus(newStatus);

            Transaction updatedTransaction =
                    transactionRepository.save(transaction);

            return convertToResponse(updatedTransaction);

        } catch (IllegalArgumentException e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid transaction status"
            );
        }
    }

    // Convert Entity to Response DTO
    private TransactionResponse convertToResponse(
            Transaction transaction) {

        TransactionResponse response =
                new TransactionResponse();

        response.setTransactionId(
                transaction.getTransactionId());

        response.setCustomerId(
                transaction.getCustomerId());

        response.setAmount(
                transaction.getAmount());

        response.setCurrency(
                transaction.getCurrency());

        response.setTransactionType(
                transaction.getTransactionType());

        response.setTransactionStatus(
                transaction.getTransactionStatus());

        return response;
    }
}