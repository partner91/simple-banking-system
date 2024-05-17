package hr.hrsak.bankingdemo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionDetailsDTO {
    private String transactionId;
    private BigDecimal balance;
    private BigDecimal oldBalance;
    private BigDecimal newBalance;
    private String email;
    private Boolean expense;
}

