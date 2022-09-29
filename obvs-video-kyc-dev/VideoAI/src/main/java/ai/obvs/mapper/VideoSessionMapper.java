package ai.obvs.mapper;

import ai.obvs.dto.VideoSessionAttendeesDto;
import ai.obvs.dto.VideoSessionDto;
import ai.obvs.model.VideoSession;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VideoSessionMapper {
    VideoSessionMapper MAPPER = Mappers.getMapper(VideoSessionMapper.class);

    VideoSession ToVideoSession(VideoSessionDto videoSessionDto);

    VideoSessionDto ToVideoSessionDto(VideoSession videoSession);
    VideoSessionAttendeesDto ToVideoSessionAttendeeDto(VideoSession videoSession);
}
