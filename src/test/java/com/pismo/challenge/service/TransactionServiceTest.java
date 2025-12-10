package com.pismo.challenge.service;

import com.pismo.challenge.domain.entity.Account;
import com.pismo.challenge.domain.entity.Transaction;
import com.pismo.challenge.domain.enums.OperationType;
import com.pismo.challenge.dto.request.CreateTransactionRequest;
import com.pismo.challenge.exception.AccountNotFoundException;
import com.pismo.challenge.repository.AccountRepository;
import com.pismo.challenge.repository.TransactionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldSavePositiveAmountWhenCreditVoucher() {
        CreateTransactionRequest request = mock(CreateTransactionRequest.class);
        Long accountId = 1L;
        when(request.accountId()).thenReturn(accountId);
        when(request.operationTypeId()).thenReturn(OperationType.CREDIT_VOUCHER.getId());
        when(request.amount()).thenReturn(new BigDecimal("100.00"));

        Account account = mock(Account.class);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        transactionService.createTransaction(request);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction saved = captor.getValue();

        assertEquals(0, saved.getAmount().compareTo(new BigDecimal("100.00")));
        assertEquals(OperationType.CREDIT_VOUCHER.getId(), saved.getOperationTypeId());
        assertSame(account, saved.getAccount());
    }

    @Test
    void shouldSaveNegativeAmountForOtherOperationTypes() {
        CreateTransactionRequest request = mock(CreateTransactionRequest.class);
        Long accountId = 2L;
        when(request.accountId()).thenReturn(accountId);

        OperationType nonCredit = Arrays.stream(OperationType.values())
                .filter(o -> o != OperationType.CREDIT_VOUCHER)
                .findFirst()
                .orElseThrow();
        when(request.operationTypeId()).thenReturn(nonCredit.getId());
        when(request.amount()).thenReturn(new BigDecimal("50.00"));

        Account account = mock(Account.class);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        transactionService.createTransaction(request);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction saved = captor.getValue();

        assertEquals(0, saved.getAmount().compareTo(new BigDecimal("-50.00")));
        assertEquals(nonCredit.getId(), saved.getOperationTypeId());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        CreateTransactionRequest request = mock(CreateTransactionRequest.class);

        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transactionService.createTransaction(request));
        verify(transactionRepository, never()).save(any());
    }
}