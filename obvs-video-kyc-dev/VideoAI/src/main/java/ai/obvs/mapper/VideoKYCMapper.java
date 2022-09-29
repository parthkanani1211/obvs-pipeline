package ai.obvs.mapper;

import ai.obvs.dto.VideoKYCRequestBaseDto;
import ai.obvs.model.VideoKYC;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface VideoKYCMapper {
    VideoKYCMapper MAPPER = Mappers.getMapper(VideoKYCMapper.class);

    VideoKYC ToVideoKYC(VideoKYCRequestBaseDto videoKYCRequestBaseDto);

    VideoKYCRequestBaseDto ToVideoKYCBaseDto(VideoKYC videoKYC);

    List<VideoKYCRequestBaseDto> ToVideoKYCBaseDto(List<VideoKYC> videoKYCSet);
}
