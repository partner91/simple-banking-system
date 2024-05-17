package hr.hrsak.bankingdemo.cronjob;

import hr.hrsak.bankingdemo.services.TransactionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PastMonthTurnoverCron {

    private final TransactionService transactionService;

    public PastMonthTurnoverCron(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Scheduled(cron = "${cron.expression}")
    public void pastMonthTurnover() {
        transactionService.updatePastMonthTurnover();
    }

}
