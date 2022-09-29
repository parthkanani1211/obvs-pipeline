package ai.obvs.mapper;

import ai.obvs.dto.VKYCResponseDto;
import ai.obvs.dto.VideoKYCRequestBaseDto;
import ai.obvs.model.VideoKYC;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface VKYCRequestMapper {
    VKYCRequestMapper MAPPER = Mappers.getMapper(VKYCRequestMapper.class);

    VKYCResponseDto TOVKYCResponseDTO(VideoKYC videoKYC);

    List<VKYCResponseDto> TOVKYCResponseDTOList(List<VideoKYC> videoKYCSet);
}
