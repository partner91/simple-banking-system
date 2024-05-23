package hr.hrsak.bankingdemo.repository;

import hr.hrsak.bankingdemo.models.Account;
import hr.hrsak.bankingdemo.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE (t.senderAccount.customer.customerId = :customerId OR t.receiverAccount.customer.customerId = :customerId)")
    List<Transaction> findByCustomerId(Integer customerId);

    @Query("SELECT t FROM Transaction t WHERE t.receiverAccount = :receiverAccount AND t.timeStamp >= :pastMonthDate")
    List<Transaction> findAllByReceiverAccountIdForPastMonth(@Param("receiverAccount") Account receiverAccount, @Param("pastMonthDate") LocalDateTime pastMonthDate);

    @Query("SELECT t FROM Transaction t WHERE t.senderAccount = :senderAccount AND t.timeStamp >= :pastMonthDate")
    List<Transaction> findAllBySenderAccountIdForPastMonth(@Param("senderAccount") Account senderAccount, @Param("pastMonthDate") LocalDateTime pastMonthDate);
}
