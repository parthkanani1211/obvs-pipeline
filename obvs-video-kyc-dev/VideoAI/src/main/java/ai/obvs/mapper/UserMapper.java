package ai.obvs.mapper;

import ai.obvs.dto.UserDto;
import ai.obvs.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
    User ToUser(UserDto genericUserDto);
    UserDto ToUserDto(User user);
}
