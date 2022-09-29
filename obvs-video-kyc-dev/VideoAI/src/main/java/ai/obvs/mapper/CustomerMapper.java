package ai.obvs.mapper;

import ai.obvs.dto.CustomerDto;
import ai.obvs.dto.CustomerServiceDto;
import ai.obvs.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper MAPPER = Mappers.getMapper(CustomerMapper.class);

    Customer ToCustomer(CustomerDto customerDto);
    Customer ToCustomer(CustomerServiceDto customerServiceDto);
    CustomerDto ToCustomerDto(Customer customer);
    CustomerServiceDto ToCustomerServiceDto(Customer customer);
}
