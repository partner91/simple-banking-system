package hr.hrsak.bankingdemo.services;

import hr.hrsak.bankingdemo.events.TransactionFinishedEvent;


public interface EmailService {

    /***
     * Method will send email to both parties involved in transaction
     * @param transactionFinishedEvent - Event with transactional details
     */
    void sendEmail(TransactionFinishedEvent transactionFinishedEvent);
}
