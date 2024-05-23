package hr.hrsak.bankingdemo.services;

import hr.hrsak.bankingdemo.dto.CustomerDTO;

public interface CustomerService{

    /***
     * Method will try to find customer with a given int id
     * @param customerId customer id
     * @return CustomerDTO - customer data transfer object
     */
    CustomerDTO findByCustomerId(Integer customerId);
}
