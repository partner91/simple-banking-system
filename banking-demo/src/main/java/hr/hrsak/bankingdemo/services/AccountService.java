package hr.hrsak.bankingdemo.services;

import hr.hrsak.bankingdemo.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    /***
     * Method will try to find account with a given ID
     * @param accountId - accountID
     * @return Optional of Account
     */

    Optional<Account> findFirstByAccountId(int accountId);

    /***
     * Method returns all accounts from DB
     * @return List of accounts
     */
    List<Account>  findAll();

    /**
     * Method that will update the account in the DB
     * @param account - Account object
     */
    void updateAccount(Account account);
}
