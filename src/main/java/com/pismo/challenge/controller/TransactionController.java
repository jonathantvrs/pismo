package com.pismo.challenge.controller;

import com.pismo.challenge.dto.request.CreateTransactionRequest;
import com.pismo.challenge.service.TransactionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody CreateTransactionRequest createTransactionRequest) {
        transactionService.createTransaction(createTransactionRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
