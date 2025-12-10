package com.pismo.challenge.service;

import com.pismo.challenge.domain.entity.Account;
import com.pismo.challenge.dto.request.CreateAccountRequest;
import com.pismo.challenge.dto.response.CreateAccountResponse;
import com.pismo.challenge.exception.AccountAlreadyExistsException;
import com.pismo.challenge.repository.AccountRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldCreateAccountWithSuccess() {
        CreateAccountRequest request = new CreateAccountRequest("12345678900");

        when(accountRepository.existsByDocumentNumber(request.documentNumber())).thenReturn(false);

        Account savedAccount = mock(Account.class);
        when(savedAccount.getAccountId()).thenReturn(1L);
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        CreateAccountResponse response = accountService.createAccount(request);

        assertNotNull(response);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        assertEquals(request.documentNumber(), captor.getValue().getDocumentNumber());

        verify(accountRepository).existsByDocumentNumber(request.documentNumber());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenAccountAlreadyExists() {
        CreateAccountRequest request = new CreateAccountRequest("12345678900");

        when(accountRepository.existsByDocumentNumber(request.documentNumber())).thenReturn(true);

        assertThrows(AccountAlreadyExistsException.class, () -> accountService.createAccount(request));

        verify(accountRepository).existsByDocumentNumber(request.documentNumber());
        verify(accountRepository, never()).save(any());
    }
}