package hr.hrsak.bankingdemo.services.impl;

import hr.hrsak.bankingdemo.dto.TransactionDTO;
import hr.hrsak.bankingdemo.dto.TransactionDetailsDTO;
import hr.hrsak.bankingdemo.mappers.TransactionMapper;
import hr.hrsak.bankingdemo.models.Account;
import hr.hrsak.bankingdemo.models.Transaction;
import hr.hrsak.bankingdemo.publishers.TransactionEventPublisher;
import hr.hrsak.bankingdemo.repository.TransactionRepository;
import hr.hrsak.bankingdemo.services.AccountService;
import hr.hrsak.bankingdemo.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionEventPublisher publisher;

    public TransactionServiceImpl(AccountService accountService, TransactionRepository transactionRepository, TransactionMapper transactionMapper,
                                  TransactionEventPublisher publisher) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.publisher = publisher;
    }

    @Override
    public Integer createTransaction(TransactionDTO transaction) {
        Transaction transactionEntity = transactionMapper.fromDTO(transaction);

        Account recieverAccount = transactionEntity.getReceiverAccount();
        Account senderAccount = transactionEntity.getSenderAccount();

        BigDecimal transferAmount = transactionEntity.getAmount();
        BigDecimal senderInitialAmount = senderAccount.getBalance();
        BigDecimal receiverInitialAmount = recieverAccount.getBalance();

        recieverAccount.setBalance(receiverInitialAmount.add(transferAmount));
        senderAccount.setBalance(senderInitialAmount.subtract(transferAmount));

        accountService.updateAccount(recieverAccount);
        accountService.updateAccount(senderAccount);

        Transaction savedTransaction = transactionRepository.save(transactionEntity);

        List<TransactionDetailsDTO> transactionDetailsDTOS = extractTransactionDetails(savedTransaction.getTransactionId(), recieverAccount, senderAccount, receiverInitialAmount, senderInitialAmount, transferAmount);
        transactionDetailsDTOS.forEach(publisher::publishTransactionFinishedEvent);

        return savedTransaction.getTransactionId();
    }



    @Override
    public void updatePastMonthTurnover() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pastMonthDate = now.minus(1,  ChronoUnit.MONTHS);
        accountService.findAll().parallelStream()
                .forEach(account -> {
                    List<Transaction> incomeTransactionsForAnAccount = getIncomeTransactionsForAnAccount(account, pastMonthDate);
                    List<Transaction> expenditureTransactionsForAnAccount = getExpenditureTransactionsForAnAccount(account, pastMonthDate);
                    BigDecimal income = incomeTransactionsForAnAccount.stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal expense = expenditureTransactionsForAnAccount.stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    account.setPastMonthTurnover(income.subtract(expense));
                    accountService.updateAccount(account);
                });
        log.info("Past month turnover for all accounts updated");

    }

    @Override
    public List<Transaction> getHistoricalTransactions(int customerId, String name, BigDecimal value) {
        List<Transaction> byCustomerId = transactionRepository.findByCustomerId(customerId);

        Predicate<Transaction> hasMatchingName = transaction ->
                name == null || name.isBlank() ||
                        transaction.getReceiverAccount().getCustomer().getName().equalsIgnoreCase(name) ||
                        transaction.getSenderAccount().getCustomer().getName().equalsIgnoreCase(name);

        Predicate<Transaction> hasMatchingValue = transaction ->
                value == null || transaction.getAmount().compareTo(value) == 0;

        return byCustomerId.stream()
                .filter(hasMatchingName.and(hasMatchingValue))
                .collect(Collectors.toList());
    }

    private List<Transaction> getIncomeTransactionsForAnAccount(Account account, LocalDateTime dateTime) {
        return transactionRepository.findAllByReceiverAccountIdForPastMonth(account, dateTime);
    }

    private List<Transaction> getExpenditureTransactionsForAnAccount(Account account, LocalDateTime dateTime) {
        return transactionRepository.findAllBySenderAccountIdForPastMonth(account, dateTime);
    }

    private List<TransactionDetailsDTO> extractTransactionDetails(int transactionId, Account recieverAccount, Account senderAccount, BigDecimal receiverInitialAmount, BigDecimal senderInitialAmount, BigDecimal transferAmount) {
        return List.of(
                TransactionDetailsDTO.builder()
                        .transactionId(String.valueOf(transactionId))
                        .email(recieverAccount.getCustomer().getEmail())
                        .balance(transferAmount)
                        .oldBalance(receiverInitialAmount)
                        .newBalance(receiverInitialAmount.add(transferAmount))
                        .expense(false)
                        .build(),
                TransactionDetailsDTO.builder()
                        .transactionId(String.valueOf(transactionId))
                        .email(senderAccount.getCustomer().getEmail())
                        .balance(transferAmount)
                        .oldBalance(senderInitialAmount)
                        .newBalance(senderInitialAmount.add(transferAmount))
                        .expense(true)
                        .build()
        );
    }
}
