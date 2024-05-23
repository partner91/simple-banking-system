package hr.hrsak.bankingdemo.services.impl;

import hr.hrsak.bankingdemo.dto.TransactionDetailsDTO;
import hr.hrsak.bankingdemo.events.TransactionFinishedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    private static final String EMAIL = "email@example.com";
    private static final String TRANSACTION_ID = "1";
    private static final String RECEIPT_EMAIL = "recipient@example.com";
    private static final String SUBJECT = "Transaction processed successfully";
    private static final BigDecimal BALANCE = BigDecimal.valueOf(10);
    private static final BigDecimal OLD_BALANCE = BigDecimal.valueOf(100);
    private static final BigDecimal NEW_BALANCE = BigDecimal.valueOf(90);
    private static final Boolean IS_EXPENSE = true;
    private final String TEMPLATE = """
              Hello!
                        
              The transaction with ID: %s has been processed successfully,
              and the balance: %s has been %s from your account.
                     
              Old balance: %s
              New balance: %s
                        
              Regards,
              Your XYZ bank
            """;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private TransactionFinishedEvent transactionFinishedEvent;
    private TransactionDetailsDTO transactionDetailsDTO;
    private SimpleMailMessage expectedMailMessage;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "email", EMAIL);
        transactionFinishedEvent = Mockito.mock(TransactionFinishedEvent.class);
        transactionDetailsDTO = Mockito.mock(TransactionDetailsDTO.class);

        given(transactionFinishedEvent.getSource()).willReturn(transactionDetailsDTO);
        given(transactionDetailsDTO.getEmail()).willReturn(RECEIPT_EMAIL);
        given(transactionDetailsDTO.getTransactionId()).willReturn(TRANSACTION_ID);
        given(transactionDetailsDTO.getBalance()).willReturn(BALANCE);
        given(transactionDetailsDTO.getOldBalance()).willReturn(OLD_BALANCE);
        given(transactionDetailsDTO.getNewBalance()).willReturn(NEW_BALANCE);

        expectedMailMessage = new SimpleMailMessage();
        expectedMailMessage.setFrom(EMAIL);
        expectedMailMessage.setTo(RECEIPT_EMAIL);
        expectedMailMessage.setSubject(SUBJECT);
    }

    @Test
    void sendEmail_BalanceTaken() {
        given(transactionDetailsDTO.getExpense()).willReturn(IS_EXPENSE);

        expectedMailMessage.setText(String.format(TEMPLATE, TRANSACTION_ID, BALANCE, "taken", OLD_BALANCE, NEW_BALANCE));

        emailService.sendEmail(transactionFinishedEvent);

        verify(javaMailSender).send(expectedMailMessage);
    }

    @Test
    void sendEmail_BalanceAdded() {
        given(transactionDetailsDTO.getExpense()).willReturn(!IS_EXPENSE);

        expectedMailMessage.setText(String.format(TEMPLATE, TRANSACTION_ID, BALANCE, "added", OLD_BALANCE, NEW_BALANCE));

        emailService.sendEmail(transactionFinishedEvent);

        verify(javaMailSender).send(expectedMailMessage);
    }
}
