package hr.hrsak.bankingdemo.services.impl;

import hr.hrsak.bankingdemo.models.Account;
import hr.hrsak.bankingdemo.repository.AccountsRepository;
import hr.hrsak.bankingdemo.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountsRepository accountsRepository;

    public AccountServiceImpl(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    @Override
    public Optional<Account> findFirstByAccountId(Integer accountId) {
        return accountsRepository.findFirstByAccountId(accountId);
    }

    @Override
    public List<Account> findAll() {
        return accountsRepository.findAll();

    }

    @Override
    public void updateAccount(Account account) {
        accountsRepository.save(account);
    }
}
