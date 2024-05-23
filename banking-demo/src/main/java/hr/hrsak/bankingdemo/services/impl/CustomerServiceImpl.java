package hr.hrsak.bankingdemo.services.impl;

import hr.hrsak.bankingdemo.dto.CustomerDTO;
import hr.hrsak.bankingdemo.exceptions.CustomerNotFoundException;
import hr.hrsak.bankingdemo.mappers.CustomerMapper;
import hr.hrsak.bankingdemo.models.Customer;
import hr.hrsak.bankingdemo.repository.CustomerRepository;
import hr.hrsak.bankingdemo.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final String errorMessage = "Customer with id: %d  not found";

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;

    }

    @Override
    public CustomerDTO findByCustomerId(Integer customerId) {
        Optional<Customer> customer = customerRepository.findCustomerByCustomerId(customerId);
        if (customer.isEmpty()) {
            log.error("Customer with id: {} not found", customerId);
            throw new CustomerNotFoundException(String.format(errorMessage, customerId));
        }
        return customerMapper.toDTO(customer.get());
    }
}
