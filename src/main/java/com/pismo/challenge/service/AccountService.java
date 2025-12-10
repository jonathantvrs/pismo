package com.pismo.challenge.service;

import com.pismo.challenge.domain.entity.Account;
import com.pismo.challenge.exception.AccountAlreadyExistsException;
import com.pismo.challenge.repository.AccountRepository;
import com.pismo.challenge.dto.request.CreateAccountRequest;
import com.pismo.challenge.dto.response.AccountResponse;
import com.pismo.challenge.dto.response.CreateAccountResponse;

import com.pismo.challenge.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /*
     * Creates a new account based on the provided request.
     *
     * @param request The request containing account creation details.
     * @return An CreateAccountResponse containing the details of the created account.
     * @throws AccountAlreadyExistsException if an account with the same document number already exists.
     */
    @Transactional
    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        boolean accountExists = accountRepository.existsByDocumentNumber(request.documentNumber());

        if (accountExists) {
            throw new AccountAlreadyExistsException(
                    "Account with document number " + request.documentNumber() + " already exists.");
        }

        Account account = new Account(request.documentNumber());

        Account savedAccount = accountRepository.save(account);

        return new CreateAccountResponse(savedAccount.getAccountId());
    }

    /*
     * Retrieves an account by its ID.
     *
     * @param accountId The ID of the account to retrieve.
     * @return An AccountResponse containing the details of the retrieved account.
     * @throws RuntimeException if the account is not found.
     */
    public AccountResponse getAccountById(Long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        Account account = accountOptional.orElseThrow(() ->
                new AccountNotFoundException("Account not found with ID: " + accountId));

        return new AccountResponse(account.getAccountId(), account.getDocumentNumber());
    }
}
