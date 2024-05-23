package hr.hrsak.bankingdemo.publishers;

import hr.hrsak.bankingdemo.dto.TransactionDetailsDTO;
import hr.hrsak.bankingdemo.events.TransactionFinishedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public TransactionEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishTransactionFinishedEvent(TransactionDetailsDTO transaction) {
        eventPublisher.publishEvent(new TransactionFinishedEvent(transaction));
    }
}
