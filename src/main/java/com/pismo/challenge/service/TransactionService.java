package com.pismo.challenge.service;

import com.pismo.challenge.domain.entity.Account;
import com.pismo.challenge.domain.entity.Transaction;
import com.pismo.challenge.domain.enums.OperationType;
import com.pismo.challenge.repository.AccountRepository;
import com.pismo.challenge.repository.TransactionRepository;
import com.pismo.challenge.dto.request.CreateTransactionRequest;

import com.pismo.challenge.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /*
     * Creates a new transaction based on the provided request.
     *
     * @param request The request containing transaction creation details.
     * @throws AccountNotFoundException if the associated account is not found.
     * @throws IllegalArgumentException if the operation type ID is invalid.
     */
    @Transactional
    public void createTransaction(CreateTransactionRequest request) {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + request.accountId()));

        OperationType operationType = OperationType.fromId(request.operationTypeId());

        BigDecimal finalAmount = request.amount();
        if (operationType != OperationType.CREDIT_VOUCHER) {
            finalAmount = finalAmount.negate();
        } else {
            finalAmount = finalAmount.abs();
        }

        Transaction transaction = new Transaction(account, operationType.getId(), finalAmount);

        transactionRepository.save(transaction);
    }
}
