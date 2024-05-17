package hr.hrsak.bankingdemo.repository;

import hr.hrsak.bankingdemo.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findCustomerByCustomerId(int customerId);
}
