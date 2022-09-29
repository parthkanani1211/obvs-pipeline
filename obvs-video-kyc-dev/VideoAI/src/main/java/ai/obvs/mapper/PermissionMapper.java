package ai.obvs.mapper;

import ai.obvs.dto.PermissionDto;
import ai.obvs.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface PermissionMapper {
    PermissionMapper MAPPER = Mappers.getMapper(PermissionMapper.class);

    Set<PermissionDto> permissionSetToPermissionDtoSet(Set<Permission> permissions);
    List<PermissionDto> permissionSetToPermissionDtoList(List<Permission> permissions);

    Set<Permission> permissionDtoSetToPermissionSet(Set<PermissionDto> permissionDto);
}
