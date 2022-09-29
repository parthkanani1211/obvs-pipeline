package ai.obvs.mapper;

import ai.obvs.dto.CBRequestDto;
import ai.obvs.dto.CKYC.CustomerProfileDto;
import ai.obvs.model.CBRequest;
import ai.obvs.model.CKYCProfileData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CBRequestMapper {
    CBRequestMapper MAPPER = Mappers.getMapper(CBRequestMapper.class);

    CBRequest ToCBRequest(CBRequestDto cbRequestDto);
    CBRequestDto ToCCBRequestDto(CBRequest cbRequest);
}
