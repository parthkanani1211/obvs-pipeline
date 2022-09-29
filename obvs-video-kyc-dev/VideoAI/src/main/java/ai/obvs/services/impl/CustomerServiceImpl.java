package ai.obvs.services.impl;

import ai.obvs.dto.CustomerServiceDto;
import ai.obvs.dto.CustomerDto;
import ai.obvs.mapper.CustomerMapper;
import ai.obvs.model.*;
import ai.obvs.repository.AppRepository;
import ai.obvs.repository.CustomerRepository;
import ai.obvs.services.CustomerService;
import ai.obvs.services.OrgService;
import ai.obvs.services.RoleService;
import ai.obvs.services.UserService;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private AppRepository appRepository;
    private RoleService roleService;
    private UserService userService;
    private OrgService orgService;

    public CustomerServiceImpl(CustomerRepository customerRepository, AppRepository appRepository, UserService userService, RoleService roleService, OrgService orgService) {
        this.customerRepository = customerRepository;
        this.appRepository = appRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.orgService = orgService;
    }

    @Override
    public CustomerDto create(CustomerDto customerDto) {
        Customer customer = CustomerMapper.MAPPER.ToCustomer(customerDto);
        Optional<User> optionalUser = userService.getUserByMobileNumber(customer.getMobileNumber());
        if (optionalUser.isPresent()) {
            throw new IllegalArgumentException("User Already Exists");
        }
        Optional<Role> optionalCustomerRole = roleService.getRoleByName("Customer");
        if (optionalCustomerRole.isPresent()) {
            Set<Role> roles = new HashSet<>();
            roles.add(optionalCustomerRole.get());
            customer.setRoles(roles);
        }
        if (customer.getFirstName() == null && customer.getLastName() == null) {
            customer.setFirstName("Customer");
            int result = getRandomNumber(1001, 3000);
            customer.setLastName(String.valueOf(result));
        }
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.MAPPER.ToCustomerDto(savedCustomer);
    }


    @Override
    public CustomerDto create(Long orgId, CustomerDto customerDto) {
        Customer customer = CustomerMapper.MAPPER.ToCustomer(customerDto);
        Optional<User> optionalUser = userService.getUserByMobileNumber(customer.getMobileNumber());
        if (optionalUser.isPresent()) {
            throw new IllegalArgumentException("User Already Exists");
        }
        Optional<Role> optionalCustomerRole = roleService.getRoleByName("Customer");
        if (optionalCustomerRole.isPresent()) {
            Set<Role> roles = new HashSet<>();
            roles.add(optionalCustomerRole.get());
            customer.setRoles(roles);
        }
        if (customer.getFirstName() == null && customer.getLastName() == null) {
            customer.setFirstName("Customer");
            int result = getRandomNumber(1001, 3000);
            customer.setLastName(String.valueOf(result));
        }
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
        if (organizationById.isPresent()) {
            List<Organization> organizations = customer.getOrganizations();
            Organization organization = organizationById.get();
            if (!organizations.contains(organization)) {
                organization.getUsers().add(customer);
                organizations.add(organization);
                customer.setOrganizations(organizations);
            }
        }
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.MAPPER.ToCustomerDto(savedCustomer);
    }

    private int getRandomNumber(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    @Override
    public CustomerDto update(CustomerDto customerDto) {
        Customer customer = CustomerMapper.MAPPER.ToCustomer(customerDto);
        Optional<Customer> customerOptional = customerRepository.findById(customer.getId());
        if (customerOptional.isPresent()) {
            Customer customerToSave = customerOptional.get();
            customerToSave.setFirstName(customer.getFirstName());
            customerToSave.setLastName(customer.getLastName());
            customerToSave.setAddress(customer.getAddress());
            Customer savedCustomer = customerRepository.save(customerToSave);
            return CustomerMapper.MAPPER.ToCustomerDto(savedCustomer);
        }
        return new CustomerDto();
    }

    @Override
    public Customer update(Customer customer) {
        return customerRepository.save(customer);
    }


    @Override
    public void update(Long id, CustomerServiceDto customerServiceDto) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
//            Set<App> customerApps = customer.getApps();

            Customer customerToUpdate = CustomerMapper.MAPPER.ToCustomer(customerServiceDto);
//            Set<App> apps = customerToUpdate.getApps();
//            for (App app : apps) {
//                Optional<App> appOptional = appRepository.findById(app.getId());
//                if (appOptional.isPresent()) {
//                    customerApps.add(appOptional.get());
//                } else {
//                    throw new IllegalArgumentException("service does not exists");
//                }
//            }
//            customer.setApps(customerApps);
            Customer result = customerRepository.save(customer);
        } else {
            throw new IllegalArgumentException("Customer does not exists");
        }
        System.out.println("Customer updated");
    }

    @Override
    public Optional<Customer> getById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public boolean isCustomer(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        return customerOptional.isPresent();
    }

    @Override
    public CustomerServiceDto getCustomerServicesById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            return CustomerMapper.MAPPER.ToCustomerServiceDto(customer);
        }
        throw new NoSuchElementException("Customer not found");
    }

}
