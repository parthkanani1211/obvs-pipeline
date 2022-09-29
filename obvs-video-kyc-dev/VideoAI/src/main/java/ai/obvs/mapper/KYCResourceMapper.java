package ai.obvs.mapper;

import ai.obvs.dto.KYCDto;
import ai.obvs.dto.KYCResourcesDto;
import ai.obvs.model.KYC;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface KYCResourceMapper {
    KYCResourceMapper MAPPER = Mappers.getMapper(KYCResourceMapper.class);
    KYCResourcesDto ToKYCResourcesDto(KYC kyc);
    List<KYCResourcesDto> ToKYCResourcesDto(List<KYC> kycList);
}
