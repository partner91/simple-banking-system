package hr.hrsak.bankingdemo.services.impl;

import hr.hrsak.bankingdemo.dto.CustomerDTO;
import hr.hrsak.bankingdemo.exceptions.CustomerNotFoundException;
import hr.hrsak.bankingdemo.mappers.CustomerMapper;
import hr.hrsak.bankingdemo.models.Customer;
import hr.hrsak.bankingdemo.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private static final Integer CUSTOMER_ID = 1;
    private static final String ERROR_MSG = "Customer with id: %d  not found";


    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(customerService, "errorMessage", ERROR_MSG);
    }


    @Test
    void findByCustomerId() {
        Customer customer = Mockito.mock(Customer.class);
        CustomerDTO customerDTO = Mockito.mock(CustomerDTO.class);
        given(customerRepository.findCustomerByCustomerId(CUSTOMER_ID)).willReturn(Optional.of(customer));
        given(customerMapper.toDTO(customer)).willReturn(customerDTO);

        assertEquals(customerService.findByCustomerId(CUSTOMER_ID), customerDTO);
    }

    @Test
    void findByCustomerId_CustomerNotFound() {
        given(customerRepository.findCustomerByCustomerId(CUSTOMER_ID)).willReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.findByCustomerId(CUSTOMER_ID));
    }
}