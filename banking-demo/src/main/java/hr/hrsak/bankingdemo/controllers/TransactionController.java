package hr.hrsak.bankingdemo.controllers;

import hr.hrsak.bankingdemo.dto.TransactionDTO;
import hr.hrsak.bankingdemo.models.Transaction;
import hr.hrsak.bankingdemo.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Integer> createTransaction(@RequestBody TransactionDTO transaction) {
        return ResponseEntity.ok(transactionService.createTransaction(transaction));
    }

    @GetMapping("/history/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable("customerId") Integer customerId,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam(required = false) BigDecimal value) {
        return ResponseEntity.ok(transactionService.getHistoricalTransactions(customerId, name, value));

    }

}
