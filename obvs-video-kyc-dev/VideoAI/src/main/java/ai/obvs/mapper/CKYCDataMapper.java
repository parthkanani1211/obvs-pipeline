package ai.obvs.mapper;

import ai.obvs.dto.CKYC.CustomerProfileDto;
import ai.obvs.dto.CustomerDto;
import ai.obvs.dto.CustomerServiceDto;
import ai.obvs.model.CKYCProfileData;
import ai.obvs.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CKYCDataMapper {
    CKYCDataMapper MAPPER = Mappers.getMapper(CKYCDataMapper.class);

    CKYCProfileData ToCKYCProfileData(CustomerProfileDto customerProfileDto);
    CustomerProfileDto ToCKYCProfileDto(CKYCProfileData ckycProfileData);
}
