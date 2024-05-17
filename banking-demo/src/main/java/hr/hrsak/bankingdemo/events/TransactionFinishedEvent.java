package hr.hrsak.bankingdemo.events;

import org.springframework.context.ApplicationEvent;

public class TransactionFinishedEvent  extends ApplicationEvent {

    public TransactionFinishedEvent(Object source) {
        super(source);
    }
}
