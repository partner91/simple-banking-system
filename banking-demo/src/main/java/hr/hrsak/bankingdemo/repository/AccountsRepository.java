package hr.hrsak.bankingdemo.repository;

import hr.hrsak.bankingdemo.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findFirstByAccountId(int accountId);

    List<Account> findAll();
}
