package ai.obvs.services;

import ai.obvs.dto.CustomerDto;
import ai.obvs.dto.CustomerServiceDto;
import ai.obvs.model.Customer;

import java.util.Optional;

public interface CustomerService {
    CustomerDto create(CustomerDto customerDto);

    CustomerDto create(Long orgId, CustomerDto customerDto);

    CustomerDto update(CustomerDto customerDto);

    Customer update(Customer customer);

    void update(Long id, CustomerServiceDto customerServiceDto);
    Optional<Customer> getById(Long id);

    boolean isCustomer(Long id);

    CustomerServiceDto getCustomerServicesById(Long id);
}
