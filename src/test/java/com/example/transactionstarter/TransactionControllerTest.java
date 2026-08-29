package com.example.transactionstarter;

import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.enums.TransactionType;
import com.example.transactionstarter.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {

        String requestBody = """
                {
                    "transactionId": "TXN001",
                    "customerId": "CUST001",
                    "amount": 1000,
                    "currency": "INR",
                    "transactionType": "PAYMENT"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("TXN001"))
                .andExpect(jsonPath("$.customerId").value("CUST001"))
                .andExpect(jsonPath("$.transactionStatus").value("PENDING"));
    }

    @Test
    void shouldRejectDuplicateTransactionId() throws Exception {

        Transaction transaction = new Transaction();

        transaction.setTransactionId("TXN002");
        transaction.setCustomerId("CUST002");
        transaction.setAmount(new BigDecimal("500"));
        transaction.setCurrency("INR");
        transaction.setTransactionType(TransactionType.PAYMENT);

        transactionRepository.save(transaction);

        String requestBody = """
                {
                    "transactionId": "TXN002",
                    "customerId": "CUST002",
                    "amount": 500,
                    "currency": "INR",
                    "transactionType": "PAYMENT"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFoundForMissingTransaction() throws Exception {

        mockMvc.perform(get("/api/transactions/TXN999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectInvalidTransactionStatus() throws Exception {

        Transaction transaction = new Transaction();

        transaction.setTransactionId("TXN003");
        transaction.setCustomerId("CUST003");
        transaction.setAmount(new BigDecimal("500"));
        transaction.setCurrency("INR");
        transaction.setTransactionType(TransactionType.PAYMENT);

        transactionRepository.save(transaction);

        mockMvc.perform(
                        put("/api/transactions/TXN003/status")
                                .param("status", "INVALID")
                )
                .andExpect(status().isBadRequest());
    }
}