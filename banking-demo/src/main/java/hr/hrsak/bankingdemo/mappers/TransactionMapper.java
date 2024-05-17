package hr.hrsak.bankingdemo.mappers;

import hr.hrsak.bankingdemo.dto.TransactionDTO;
import hr.hrsak.bankingdemo.exceptions.AccountNotFoundException;
import hr.hrsak.bankingdemo.models.Transaction;
import hr.hrsak.bankingdemo.services.AccountService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper {
    
    @Autowired
    protected AccountService accountService;


    @Mapping(source = "senderAccount.accountId", target = "senderAccountId")
    @Mapping(source = "receiverAccount.accountId", target = "receiverAccountId")
    public abstract  TransactionDTO toDTO(Transaction transaction);

    @Mapping(target = "senderAccount", expression = "java(accountService.findFirstByAccountId(transactionDTO.getSenderAccountId()).orElseThrow(() -> new AccountNotFoundException(\"Sender account not found\")))")
    @Mapping(target = "receiverAccount", expression = "java(accountService.findFirstByAccountId(transactionDTO.getReceiverAccountId()).orElseThrow(() -> new AccountNotFoundException(\"Receiver account not found\")))")
    @Mapping(target = "transactionId", ignore = true)
    public abstract Transaction fromDTO(TransactionDTO transactionDTO) throws AccountNotFoundException;
}