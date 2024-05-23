package hr.hrsak.bankingdemo.services.impl;

import hr.hrsak.bankingdemo.dto.TransactionDetailsDTO;
import hr.hrsak.bankingdemo.events.TransactionFinishedEvent;
import hr.hrsak.bankingdemo.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${email}")
    private String email;
    private final JavaMailSender mailSender;
    private final String TEMPLATE = """
              Hello!
                        
              The transaction with ID: %s has been processed successfully,
              and the balance: %s has been %s from your account.
                     
              Old balance: %s
              New balance: %s
                        
              Regards,
              Your XYZ bank
            """;


    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    @Override
    public void sendEmail(TransactionFinishedEvent transactionFinishedEvent) {
        TransactionDetailsDTO transaction = (TransactionDetailsDTO) transactionFinishedEvent.getSource();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(email);
        mailMessage.setTo(transaction.getEmail());
        mailMessage.setSubject("Transaction processed successfully");
        mailMessage.setText(String.format(TEMPLATE, transaction.getTransactionId(), transaction.getBalance().toString(),
                transaction.getExpense() ? "taken" : "added",
                transaction.getOldBalance().toString(), transaction.getNewBalance().toString()));
        try {
            mailSender.send(mailMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
