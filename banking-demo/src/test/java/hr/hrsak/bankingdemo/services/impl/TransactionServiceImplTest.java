package hr.hrsak.bankingdemo.services.impl;

import hr.hrsak.bankingdemo.dto.TransactionDTO;
import hr.hrsak.bankingdemo.exceptions.AccountNotFoundException;
import hr.hrsak.bankingdemo.mappers.TransactionMapper;
import hr.hrsak.bankingdemo.models.Account;
import hr.hrsak.bankingdemo.models.Customer;
import hr.hrsak.bankingdemo.models.Transaction;
import hr.hrsak.bankingdemo.publishers.TransactionEventPublisher;
import hr.hrsak.bankingdemo.repository.TransactionRepository;
import hr.hrsak.bankingdemo.services.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private Clock clock;

    @Mock
    private TransactionEventPublisher transactionEventPublisher;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private static final BigDecimal TRANSFER_AMOUNT = new BigDecimal(100);
    private static final BigDecimal SENDER_INITIAL = new BigDecimal(100);
    private static final BigDecimal RECEIVER_INITIAL = new BigDecimal(100);
    private static final Integer TRANSACTION_ID = 1;
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    public static final Integer CUSTOMER_ID = 1;


    @Test
    void createTransaction() {
        TransactionDTO transactionDTO = Mockito.mock(TransactionDTO.class);
        Transaction transaction = Mockito.mock(Transaction.class);
        Account recieverAccount = Mockito.mock(Account.class);
        Account senderAccount = Mockito.mock(Account.class);
        Customer customer = Mockito.mock(Customer.class);

        given(transactionMapper.fromDTO(transactionDTO)).willReturn(transaction);
        given(transaction.getReceiverAccount()).willReturn(recieverAccount);
        given(transaction.getSenderAccount()).willReturn(senderAccount);
        given(transaction.getAmount()).willReturn(TRANSFER_AMOUNT);
        given(senderAccount.getBalance()).willReturn(SENDER_INITIAL);
        given(recieverAccount.getBalance()).willReturn(RECEIVER_INITIAL);
        given(transaction.getTransactionId()).willReturn(TRANSACTION_ID);
        given(transactionRepository.save(transaction)).willReturn(transaction);
        given(recieverAccount.getCustomer()).willReturn(customer);
        given(senderAccount.getCustomer()).willReturn(customer);
        given(customer.getEmail()).willReturn(EMAIL);


        assertEquals(TRANSACTION_ID, transactionService.createTransaction(transactionDTO));

        verify(accountService).updateAccount(recieverAccount);
        verify(accountService).updateAccount(senderAccount);
        verify(transactionRepository).save(transaction);
        verify(transactionEventPublisher, times(2)).publishTransactionFinishedEvent(any());
    }

    @Test
    void createTransaction_AccountNotFound() {
        TransactionDTO transactionDTO = Mockito.mock(TransactionDTO.class);

        given(transactionMapper.fromDTO(transactionDTO)).willThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> transactionService.createTransaction(transactionDTO));
    }

    @Test
    void updatePastMonthTurnover() {
        Instant fixedInstant = Instant.now();
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));
        given(clock.instant()).willReturn(fixedInstant);
        given(clock.getZone()).willReturn(fixedClock.getZone());

        LocalDateTime now = LocalDateTime.now(fixedClock);
        LocalDateTime pastMonthDate = now.minus(1, ChronoUnit.MONTHS);


        Account receiverAccount = new Account();
        receiverAccount.setPastMonthTurnover(BigDecimal.ZERO);
        receiverAccount.setBalance(BigDecimal.ZERO);
        receiverAccount.setAccountId(1);

        Account senderAccount = new Account();
        senderAccount.setPastMonthTurnover(BigDecimal.ZERO);
        senderAccount.setBalance(BigDecimal.ZERO);
        senderAccount.setAccountId(2);

        Transaction transaction = new Transaction();
        transaction.setAmount(TRANSFER_AMOUNT);
        transaction.setReceiverAccount(receiverAccount);
        transaction.setSenderAccount(senderAccount);

        given(accountService.findAll()).willReturn(List.of(senderAccount, receiverAccount));


        given(transactionRepository.findAllByReceiverAccountIdForPastMonth(receiverAccount, pastMonthDate))
                .willReturn(List.of(transaction));
        given(transactionRepository.findAllByReceiverAccountIdForPastMonth(senderAccount, pastMonthDate))
                .willReturn(Collections.emptyList());


        given(transactionRepository.findAllBySenderAccountIdForPastMonth(senderAccount, pastMonthDate))
                .willReturn(List.of(transaction));
        given(transactionRepository.findAllBySenderAccountIdForPastMonth(receiverAccount, pastMonthDate))
                .willReturn(Collections.emptyList());


        transactionService.updatePastMonthTurnover();


        assertEquals(TRANSFER_AMOUNT, receiverAccount.getPastMonthTurnover());
        assertEquals(TRANSFER_AMOUNT.negate(), senderAccount.getPastMonthTurnover());

        verify(accountService).updateAccount(receiverAccount);
        verify(accountService).updateAccount(senderAccount);

    }


    @Test
    void getHistoricalTransactions() {
        Transaction transaction = mock(Transaction.class);
        Account account = mock(Account.class);
        Customer customer = mock(Customer.class);


        given(transactionRepository.findByCustomerId(CUSTOMER_ID)).willReturn(List.of(transaction));
        given(transaction.getReceiverAccount()).willReturn(account);
        given(account.getCustomer()).willReturn(customer);
        given(customer.getName()).willReturn(NAME);
        given(transaction.getAmount()).willReturn(TRANSFER_AMOUNT);

        assertEquals(List.of(transaction), transactionService.getHistoricalTransactions(CUSTOMER_ID, NAME, TRANSFER_AMOUNT));

    }

    @Test
    void getHistoricalTransactions_ZeroRecordedTransactionsForGivenId() {

        given(transactionRepository.findByCustomerId(CUSTOMER_ID)).willReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), transactionService.getHistoricalTransactions(CUSTOMER_ID, NAME, TRANSFER_AMOUNT));

    }
}