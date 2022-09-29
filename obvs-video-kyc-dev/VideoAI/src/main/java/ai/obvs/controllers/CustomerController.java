package ai.obvs.controllers;

import ai.obvs.dto.CustomerDto;
import ai.obvs.dto.CustomerServiceDto;
import ai.obvs.mapper.CustomerMapper;
import ai.obvs.model.Customer;
import ai.obvs.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/v1/customers")
public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/")
    public ResponseEntity<?> post(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomerDto = customerService.create(customerDto);
        return ResponseEntity.ok(savedCustomerDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable(value = "id") Long id) {
        Optional<Customer> optionalCustomer = customerService.getById(id);
        if (optionalCustomer.isPresent()) {
            CustomerDto customerDto = CustomerMapper.MAPPER.ToCustomerDto(optionalCustomer.get());
            return ResponseEntity.ok(customerDto);
        }
        return ResponseEntity.ok("Customer not exists");
    }

    @PutMapping("/{id}/services")
    public ResponseEntity<?> put(@PathVariable(value = "id") Long id, @Valid @RequestBody CustomerServiceDto customerServiceDto) {
        customerService.update(id, customerServiceDto);
        return ResponseEntity.ok("");
    }

    @GetMapping("/{id}/services")
    public ResponseEntity<?> getServices(@PathVariable(value = "id") Long id) {
        CustomerServiceDto customerServiceDto = customerService.getCustomerServicesById(id);
        return ResponseEntity.ok(customerServiceDto);
    }

}
