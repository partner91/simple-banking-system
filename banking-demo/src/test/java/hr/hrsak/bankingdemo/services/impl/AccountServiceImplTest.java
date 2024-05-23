package hr.hrsak.bankingdemo.services.impl;

import hr.hrsak.bankingdemo.models.Account;
import hr.hrsak.bankingdemo.repository.AccountsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountsRepository accountsRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private static final Integer accountId = 1;

    @Test
    void findFirstByAccountId() {
        Account account = Mockito.mock(Account.class);
        given(accountsRepository.findFirstByAccountId(accountId)).willReturn(Optional.of(account));

        assertTrue(accountService.findFirstByAccountId(accountId).isPresent());
        assertEquals(accountService.findFirstByAccountId(accountId).get(), account);
    }

    @Test
    void findFirstByAccountId_AccountNotFound() {
        given(accountsRepository.findFirstByAccountId(accountId)).willReturn(Optional.empty());

        assertFalse(accountsRepository.findFirstByAccountId(accountId).isPresent());
    }

    @Test
    void findAll() {
        Account account = Mockito.mock(Account.class);
        given(accountsRepository.findAll()).willReturn(List.of(account));

        assertFalse(accountService.findAll().isEmpty());
        assertEquals(accountService.findAll().size(), 1);
        assertEquals(accountService.findAll().getFirst(), account);
    }

    @Test
    void findAll_NoAccountFound() {
        given(accountsRepository.findAll()).willReturn(List.of());
        assertTrue(accountService.findAll().isEmpty());
    }

    @Test
    void updateAccount() {
        Account account = Mockito.mock(Account.class);

        given(accountsRepository.save(account)).willReturn(account);
        accountService.updateAccount(account);

        verify(accountsRepository).save(account);

    }
}