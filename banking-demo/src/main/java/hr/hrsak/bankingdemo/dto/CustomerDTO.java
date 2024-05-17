package hr.hrsak.bankingdemo.dto;

import hr.hrsak.bankingdemo.models.Account;
import lombok.Data;

import java.util.List;

@Data
public class CustomerDTO {
    private int customerId;
    private String name;
    private List<Account> accounts;
}
