package ai.obvs.controllers;

import ai.obvs.dto.CKYC.CustomerProfileDto;
import ai.obvs.dto.CustomerDto;
import ai.obvs.dto.CustomerServiceDto;
import ai.obvs.mapper.CustomerMapper;
import ai.obvs.model.CBRequest;
import ai.obvs.model.Customer;
import ai.obvs.repository.CustomerRepository;
import ai.obvs.security.CurrentUser;
import ai.obvs.security.UserPrincipal;
import ai.obvs.services.CBRequestsService;
import ai.obvs.services.CKYC.CKYCDataService;
import ai.obvs.services.CustomerService;
import ai.obvs.services.notification.PropertiesData;
import liquibase.pro.packaged.A;
import liquibase.pro.packaged.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/v1/customer")
public class CustomerProfileController {

    @Autowired
    private CKYCDataService ckycDataService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CBRequestsService cbRequestsService;
    @Autowired
    private PropertiesData propertiesData;

    @PostMapping("/profile")
    public ResponseEntity<?> saveProfileData(@RequestHeader("x-obvs-org") Long orgId,
                                             @Valid @RequestBody CustomerProfileDto customerProfileDto,
                                             @CurrentUser UserPrincipal currentUser) {
        Long userId = currentUser.getUser().getId();
        Optional<Customer> customerOptional = customerService.getById(userId);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();

            if (propertiesData.getCkycEnabled()) {
                if (customerProfileDto.getRefId().isBlank())
                    throw new IllegalArgumentException("Unable to process due to request mismatch. Please try to re-login and perform Video KYC.");

                CBRequest cbRequestByRefId = cbRequestsService.getCBRequestByRefId(customerProfileDto.getRefId());
            }

            CustomerProfileDto savedCustomerProfileDto = ckycDataService.save(customer, customerProfileDto);
            customer.setFirstName(savedCustomerProfileDto.getCustomerName().getFirstName());
            customer.setLastName(savedCustomerProfileDto.getCustomerName().getLastName());
            customerService.update(customer);
            return ResponseEntity.ok(savedCustomerProfileDto);
        } else {
            throw new IllegalArgumentException("Customer not exists");
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfileData(@RequestHeader("x-obvs-org") Long orgId,
                                               @Valid @RequestBody CustomerProfileDto customerProfileDto,
                                               @CurrentUser UserPrincipal currentUser) {
        Long userId = currentUser.getUser().getId();
        Optional<Customer> customerOptional = customerService.getById(userId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            CustomerProfileDto savedCustomerProfileDto = ckycDataService.update(customer, customerProfileDto);
            customer.setFirstName(savedCustomerProfileDto.getCustomerName().getFirstName());
            customer.setLastName(savedCustomerProfileDto.getCustomerName().getLastName());
            customerService.update(customer);
            return ResponseEntity.ok(savedCustomerProfileDto);
        } else {
            throw new IllegalArgumentException("Customer not exists");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfileData(@RequestHeader("x-obvs-org") Long orgId,
                                            @CurrentUser UserPrincipal currentUser) {
        Long userId = currentUser.getUser().getId();
        CustomerProfileDto customerProfileDto = ckycDataService.findByCustomerId(userId);
        return ResponseEntity.ok(customerProfileDto);
    }
}
