package hr.hrsak.bankingdemo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountTypeEnum accountType;

    private BigDecimal balance;

    @Column(name = "past_month_turnover")
    private BigDecimal pastMonthTurnover;

    @ManyToOne
    @JoinColumn(name="customerId")
    @JsonIgnore
    private Customer customer;

    public Account(Integer accountId) {
        this.accountId = accountId;
    }
}
