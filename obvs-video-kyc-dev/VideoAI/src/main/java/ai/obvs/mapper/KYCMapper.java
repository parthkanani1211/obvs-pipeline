package ai.obvs.mapper;

import ai.obvs.dto.KYCDto;
import ai.obvs.model.KYC;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface KYCMapper {
    KYCMapper MAPPER = Mappers.getMapper(KYCMapper.class);

    @Mapping(target = "KYCVerificationData", ignore = true)
    KYC ToKYC(KYCDto kycDto);

    @Mapping(target = "kycVerificationData", ignore = true)
    KYCDto ToKYCDto(KYC kyc);

    List<KYCDto> ToKYCDto(List<KYC> kycList);
}
