package ai.obvs.mapper;

import ai.obvs.dto.RoleBasicDto;
import ai.obvs.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoleMapper {
    RoleMapper MAPPER = Mappers.getMapper(RoleMapper.class);

    RoleBasicDto roleToRoleBasicDto(Role role);

    Role roleBasicDtoToRole(RoleBasicDto roleBasicDto);

    List<RoleBasicDto> roleListToRoleBasicDtoList(List<Role> roles);
}