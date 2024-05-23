package hr.hrsak.bankingdemo.services;

import hr.hrsak.bankingdemo.dto.TransactionDTO;
import hr.hrsak.bankingdemo.models.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    /**
     * This API endpoint takes a transaction object and processes the transaction.​
     * @param transaction - transaction
     * @return Returns the ID of the stored transaction.
     */
    Integer createTransaction(TransactionDTO transaction);

    /***
     * Method that will update accounts field with previous month turnover
     */
    void updatePastMonthTurnover();

    /***
     * Method that returns historical transactional data for a given customer
     * @param customerId - customer id
     * @param name - name
     * @param value - value
     * @return List of transactions
     */
    List<Transaction> getHistoricalTransactions(Integer customerId, String name, BigDecimal value);

}
